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
import com.glyphsynapse.app.service.GlyphAnimationService
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AnimationsUiState(
    val animations: List<AnimationDefinition> = emptyList(),
    val selectedName: String = "Breathe",
    val speedMultiplier: Float = 1f,
    val device: GlyphMatrixDevice = GlyphMatrixDevice.Stub
)

@HiltViewModel
class AnimationsViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val prefs: UserPreferencesRepository,
    private val glyphManager: GlyphManagerWrapper,
    private val animationPlayer: AnimationPlayer
) : ViewModel() {

    val pixelFrame = animationPlayer.currentFrame

    init {
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

    val uiState: StateFlow<AnimationsUiState> = combine(
        prefs.selectedAnimation,
        prefs.speedMultiplier
    ) { selected, speed ->
        AnimationsUiState(
            animations = AnimationRegistry.all(context),
            selectedName = selected,
            speedMultiplier = speed,
            device = glyphManager.device
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), AnimationsUiState())

    fun selectAnimation(name: String) {
        viewModelScope.launch {
            prefs.setSelectedAnimation(name)
            GlyphAnimationService.notifyUpdate(context)
        }
    }
}
