package com.glyphsynapse.app.service

import android.graphics.drawable.Icon
import android.service.quicksettings.Tile
import android.service.quicksettings.TileService
import com.glyphsynapse.app.R
import com.glyphsynapse.app.data.datastore.UserPreferencesRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class GlyphTileService : TileService() {

    @Inject lateinit var prefs: UserPreferencesRepository

    private val scope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    override fun onStartListening() {
        scope.launch {
            val enabled = prefs.serviceEnabled.first()
            updateTile(enabled)
        }
    }

    override fun onClick() {
        scope.launch {
            val current = prefs.serviceEnabled.first()
            val newState = !current
            prefs.setServiceEnabled(newState)
            if (newState) {
                GlyphAnimationService.start(applicationContext)
            } else {
                GlyphAnimationService.stop(applicationContext)
            }
            updateTile(newState)
            Timber.d("Tile toggled — service enabled: $newState")
        }
    }

    override fun onStopListening() {
        scope.cancel()
    }

    private fun updateTile(enabled: Boolean) {
        qsTile?.apply {
            state = if (enabled) Tile.STATE_ACTIVE else Tile.STATE_INACTIVE
            icon = Icon.createWithResource(applicationContext, R.drawable.ic_tile_glyph)
            updateTile()
        }
    }
}
