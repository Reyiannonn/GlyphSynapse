package com.glyphsynapse.app.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.glyphsynapse.app.data.datastore.UserPreferencesRepository
import com.glyphsynapse.app.data.glyph.GlyphMatrixDevice
import com.glyphsynapse.app.data.glyph.GlyphManagerWrapper
import com.glyphsynapse.app.domain.animation.AnimationDefinition
import com.glyphsynapse.app.domain.animation.AnimationRegistry
import com.glyphsynapse.app.domain.engine.AnimationPlayer
import com.glyphsynapse.app.domain.model.PixelFrame
import com.glyphsynapse.app.service.GlyphAnimationService
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Main Home UI State.
 * Does NOT contain pixels to prevent full-screen recomposition on every frame.
 */
data class HomeUiState(
    val serviceEnabled: Boolean = false,
    val selectedAnimation: AnimationDefinition? = null,
    val speedMultiplier: Float = 1f,
    val brightness: Float = 0.8f,
    val stealthMode: Boolean = false,
    val device: GlyphMatrixDevice = GlyphMatrixDevice.Stub,
    val isCompatibleDevice: Boolean = false
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val prefs: UserPreferencesRepository,
    private val glyphManager: GlyphManagerWrapper,
    private val animationPlayer: AnimationPlayer
) : ViewModel() {

    /** Exposes the raw frame stream separately for the preview component. */
    val pixelFrame: StateFlow<PixelFrame?> = animationPlayer.currentFrame

    init {
        // Sync preferences to the engine
        viewModelScope.launch {
            combine(
                prefs.selectedAnimation,
                prefs.speedMultiplier,
                prefs.brightness,
                prefs.stealthMode
            ) { animName, speed, brightness, stealth ->
                val animation = AnimationRegistry.find(animName, context)
                val finalBrightness = if (stealth) brightness * 0.15f else brightness
                animationPlayer.play(animation, speed, finalBrightness, glyphManager.device, forceReset = false)
            }.collect {}
        }
    }

    val uiState: StateFlow<HomeUiState> = combine(
        prefs.serviceEnabled,
        prefs.selectedAnimation,
        prefs.speedMultiplier,
        prefs.brightness,
        prefs.stealthMode
    ) { args ->
        HomeUiState(
            serviceEnabled = args[0] as Boolean,
            selectedAnimation = AnimationRegistry.find(args[1] as String, context),
            speedMultiplier = args[2] as Float,
            brightness = args[3] as Float,
            stealthMode = args[4] as Boolean,
            device = glyphManager.device,
            isCompatibleDevice = glyphManager.isCompatibleDevice
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), HomeUiState())

    fun toggleService(enabled: Boolean) {
        viewModelScope.launch {
            prefs.setServiceEnabled(enabled)
            if (enabled) GlyphAnimationService.start(context)
            else GlyphAnimationService.stop(context)
        }
    }
}
