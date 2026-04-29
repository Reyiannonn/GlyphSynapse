package com.glyphsynapse.app.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.glyphsynapse.app.data.datastore.UserPreferencesRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class BootReceiver : BroadcastReceiver() {

    @Inject lateinit var prefs: UserPreferencesRepository

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != Intent.ACTION_BOOT_COMPLETED &&
            intent.action != Intent.ACTION_MY_PACKAGE_REPLACED) return

        Timber.d("Boot/update received — checking service preference")
        val pendingResult = goAsync()

        CoroutineScope(Dispatchers.IO).launch {
            try {
                if (prefs.serviceEnabled.first()) {
                    GlyphAnimationService.start(context)
                    Timber.d("Service auto-started after boot")
                }
            } finally {
                pendingResult.finish()
            }
        }
    }
}
