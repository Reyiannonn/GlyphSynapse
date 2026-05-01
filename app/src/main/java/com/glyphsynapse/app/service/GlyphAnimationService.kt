package com.glyphsynapse.app.service

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.content.pm.ServiceInfo
import android.os.BatteryManager
import android.os.PowerManager
import androidx.core.content.ContextCompat
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.lifecycleScope
import com.glyphsynapse.app.MainActivity
import com.glyphsynapse.app.R
import com.glyphsynapse.app.data.datastore.UserPreferencesRepository
import com.glyphsynapse.app.data.glyph.GlyphManagerWrapper
import com.glyphsynapse.app.domain.animation.AnimationRegistry
import com.glyphsynapse.app.domain.engine.AnimationPlayer
import com.glyphsynapse.app.domain.engine.AudioAwareness
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class GlyphAnimationService : LifecycleService() {

    @Inject lateinit var glyphManager: GlyphManagerWrapper
    @Inject lateinit var animationPlayer: AnimationPlayer
    @Inject lateinit var audioAwareness: AudioAwareness
    @Inject lateinit var prefs: UserPreferencesRepository

    private var wakeLock: PowerManager.WakeLock? = null
    private var screenIsOff = false

    private val screenReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            when (intent.action) {
                Intent.ACTION_SCREEN_OFF -> {
                    screenIsOff = true
                    acquireWakeLock()
                    lifecycleScope.launch { refreshAnimation(forceReset = false) }
                }
                Intent.ACTION_SCREEN_ON -> {
                    screenIsOff = false
                    releaseWakeLock()
                }
            }
        }
    }

    private val chargingReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (animationPlayer.isPlaying) lifecycleScope.launch { refreshAnimation() }
        }
    }

    override fun onCreate() {
        super.onCreate()
        
        val hasMicPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED
        
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            var fgsType = ServiceInfo.FOREGROUND_SERVICE_TYPE_SPECIAL_USE
            if (hasMicPermission) {
                fgsType = fgsType or ServiceInfo.FOREGROUND_SERVICE_TYPE_MICROPHONE
            }
            
            startForeground(
                NOTIFICATION_ID, 
                buildNotification(),
                fgsType
            )
        } else {
            startForeground(NOTIFICATION_ID, buildNotification())
        }
        
        if (hasMicPermission) {
            audioAwareness.start()
        } else {
            Timber.w("Microphone permission missing — audio visualizer disabled")
        }

        glyphManager.onConnected = { lifecycleScope.launch { refreshAnimation(forceReset = true) } }
        glyphManager.init()

        registerReceiver(
            screenReceiver,
            IntentFilter().apply {
                addAction(Intent.ACTION_SCREEN_OFF)
                addAction(Intent.ACTION_SCREEN_ON)
            },
        )
        registerReceiver(chargingReceiver, IntentFilter(Intent.ACTION_BATTERY_CHANGED))

        Timber.d("GlyphAnimationService started")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        val action = intent?.action
        Timber.d("GlyphAnimationService onStartCommand action=$action")

        if (((action == ACTION_UPDATE_ANIMATION || action == ACTION_GLYPH_TOY)) && glyphManager.isConnected.value) {
            lifecycleScope.launch { refreshAnimation(forceReset = false) }
        }
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        audioAwareness.stop()
        animationPlayer.release()
        glyphManager.release()
        runCatching { unregisterReceiver(screenReceiver) }
        runCatching { unregisterReceiver(chargingReceiver) }
        wakeLock?.release()
        Timber.d("GlyphAnimationService destroyed")
    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        @Suppress("DEPRECATION")
        if (level >= TRIM_MEMORY_RUNNING_CRITICAL) {
            Timber.w("Memory pressure $level — pausing animation")
            animationPlayer.pause()
        }
    }

    private suspend fun refreshAnimation(forceReset: Boolean = false) {
        val animName      = prefs.selectedAnimation.first()
        val speed         = prefs.speedMultiplier.first()
        val rawBrightness = prefs.brightness.first()
        val stealth       = prefs.stealthMode.first()
        val chargingLock  = prefs.chargingLock.first()

        val brightness    = if (stealth) rawBrightness * 0.15f else rawBrightness
        val isCharging    = isDeviceCharging()
        val resolvedName  = if (chargingLock && isCharging) "Charging Fill" else animName

        val animation = AnimationRegistry.find(resolvedName, this)
        animationPlayer.play(animation, speed, brightness, glyphManager.device, forceReset)

        // Always ensure timeout is disabled
        glyphManager.setTimeoutEnabled(false)
    }

    private fun isDeviceCharging(): Boolean {
        val intent = registerReceiver(null, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
        val status = intent?.getIntExtra(BatteryManager.EXTRA_STATUS, -1) ?: -1
        return status == BatteryManager.BATTERY_STATUS_CHARGING ||
               status == BatteryManager.BATTERY_STATUS_FULL
    }

    private fun acquireWakeLock() {
        if (wakeLock?.isHeld == true) return
        val pm = getSystemService(POWER_SERVICE) as PowerManager
        wakeLock = pm.newWakeLock(
            PowerManager.PARTIAL_WAKE_LOCK,
            "GlyphSynapse::AnimationWakeLock"
        ).apply { acquire() } // 24/7 operation, no timeout
    }

    private fun releaseWakeLock() {
        wakeLock?.takeIf { it.isHeld }?.release()
        wakeLock = null
    }

    private fun buildNotification(): Notification {
        val channelId = "glyph_synapse_aod"
        val nm = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        if (nm.getNotificationChannel(channelId) == null) {
            nm.createNotificationChannel(
                NotificationChannel(channelId, getString(R.string.notification_channel_name),
                    NotificationManager.IMPORTANCE_MIN).apply {
                    description = getString(R.string.notification_channel_desc)
                }
            )
        }
        val tap = PendingIntent.getActivity(this, 0, Intent(this, MainActivity::class.java),
            PendingIntent.FLAG_IMMUTABLE)
        return NotificationCompat.Builder(this, channelId)
            .setContentTitle(getString(R.string.notification_title))
            .setContentText(getString(R.string.notification_text))
            .setSmallIcon(R.drawable.ic_tile_glyph)
            .setContentIntent(tap)
            .setOngoing(true)
            .setSilent(true)
            .setPriority(NotificationCompat.PRIORITY_MIN)
            .build()
    }

    companion object {
        private const val NOTIFICATION_ID = 1001
        const val ACTION_UPDATE_ANIMATION = "com.glyphsynapse.app.ACTION_UPDATE_ANIMATION"
        const val ACTION_GLYPH_TOY = "com.nothing.glyph.toy.ACTION_GLYPH_TOY"

        fun start(context: Context) =
            context.startForegroundService(Intent(context, GlyphAnimationService::class.java))

        fun stop(context: Context) =
            context.stopService(Intent(context, GlyphAnimationService::class.java))

        /** Call after changing animation/speed/brightness to refresh the running animation. */
        fun notifyUpdate(context: Context) {
            val intent = Intent(context, GlyphAnimationService::class.java)
                .setAction(ACTION_UPDATE_ANIMATION)
            context.startService(intent)
        }
    }
}
