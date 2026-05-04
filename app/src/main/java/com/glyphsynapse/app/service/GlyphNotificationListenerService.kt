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
    private var yieldJob: Job? = null

    override fun onNotificationPosted(sbn: StatusBarNotification) {
        if (!glyphManager.isConnected.value) return
        
        // Check if it's a notification we should yield for
        val category = sbn.notification.category
        val isUrgent = category == android.app.Notification.CATEGORY_CALL || 
            category == android.app.Notification.CATEGORY_MESSAGE ||
            category == android.app.Notification.CATEGORY_ALARM
            
        if (isUrgent) {
            Timber.d("GlyphNotificationListener: Yielding matrix for notification from ${sbn.packageName}")
            
            yieldJob?.cancel()
            yieldJob = scope.launch {
                // Stop our animations and clear matrix so OS can take over
                glyphManager.setFocus(false)
                glyphManager.clear()
                
                // Wait for the OS notification animation to finish (typical duration 5-8 seconds)
                delay(6000)
                
                // Resume our animations
                Timber.d("GlyphNotificationListener: Resuming matrix control")
                glyphManager.setFocus(true)
            }
        }
    }

    override fun onListenerDisconnected() {
        scope.cancel()
    }
}
