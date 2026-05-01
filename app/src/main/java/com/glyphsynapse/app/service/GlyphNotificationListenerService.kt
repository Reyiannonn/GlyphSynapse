package com.glyphsynapse.app.service

import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import com.glyphsynapse.app.data.glyph.GlyphManagerWrapper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class GlyphNotificationListenerService : NotificationListenerService() {

    @Inject lateinit var glyphManager: GlyphManagerWrapper

    private val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())
    private var reactionJob: Job? = null

    override fun onNotificationPosted(sbn: StatusBarNotification) {
        if (!glyphManager.isConnected.value) return
        val category = sbn.notification.category ?: return
        Timber.d("Notification category: $category from ${sbn.packageName}")

        reactionJob?.cancel()
        reactionJob = scope.launch {
            when (category) {
                android.app.Notification.CATEGORY_CALL    -> reactPulse(pulses = 8, onMs = 150, offMs = 150)
                android.app.Notification.CATEGORY_MESSAGE -> reactPulse(pulses = 2, onMs = 300, offMs = 200)
                android.app.Notification.CATEGORY_ALARM   -> reactPulse(pulses = 6, onMs = 200, offMs = 300)
            }
        }
    }

    /**
     * Flash the entire matrix on/off [pulses] times.
     * A full-white frame alternates with a clear frame.
     */
    private suspend fun reactPulse(pulses: Int, onMs: Long, offMs: Long) {
        val device = glyphManager.device
        val fullOn = IntArray(device.matrixSize) { 255 }

        repeat(pulses) { i ->
            if ((i % 2 == 0)) {
                glyphManager.sendFrame(fullOn)
                delay(onMs)
            } else {
                glyphManager.clear()
                delay(offMs)
            }
        }
        glyphManager.clear()
    }

    override fun onListenerDisconnected() {
        scope.cancel()
    }
}
