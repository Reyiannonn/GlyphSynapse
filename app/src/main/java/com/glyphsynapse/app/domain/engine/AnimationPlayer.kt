package com.glyphsynapse.app.domain.engine

import com.glyphsynapse.app.data.glyph.GlyphMatrixDevice
import com.glyphsynapse.app.data.glyph.GlyphManagerWrapper
import com.glyphsynapse.app.domain.animation.AnimationDefinition
import com.glyphsynapse.app.domain.model.PixelFrame
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AnimationPlayer @Inject constructor(
    private val glyphManager: GlyphManagerWrapper,
    private val audioAwareness: AudioAwareness
) {
    private var playerScope = CoroutineScope(Dispatchers.Default)
    private var animationJob: Job? = null
    private val scheduler = FrameScheduler()

    @Volatile var isPlaying: Boolean = false
        private set

    private val _currentFrame = MutableStateFlow<PixelFrame?>(null)
    val currentFrame: StateFlow<PixelFrame?> = _currentFrame.asStateFlow()

    private val _elapsedTime = MutableStateFlow(0L)
    val elapsedTime: StateFlow<Long> = _elapsedTime.asStateFlow()

    private var currentAnimationName: String? = null

    init {
        // Start monitoring audio immediately
        audioAwareness.start()
    }

    /**
     * Starts or updates the animation.
     */
    fun play(
        animation: AnimationDefinition,
        speedMultiplier: Float,
        brightness: Float,
        device: GlyphMatrixDevice,
        forceReset: Boolean = false
    ) {
        val isSameAnimation = currentAnimationName == animation.name
        
        if (!isSameAnimation || !isPlaying || forceReset) {
            if (forceReset) scheduler.reset()
            currentAnimationName = animation.name
            stopJobOnly()
        } else {
            // Already running, just updating params via closure references
            return
        }

        isPlaying = true

        animationJob = playerScope.launch {
            Timber.d("AnimationPlayer: running ${animation.name}")
            while (isActive) {
                // Fetch current audio data
                val energy = audioAwareness.energy.value
                val bass = audioAwareness.bass.value
                val mid = audioAwareness.mid.value
                
                // Fix: Move the rhythmic acceleration into the scheduler
                // so it accumulates delta-speed instead of multiplying total time.
                val effectiveSpeed = speedMultiplier * (1f + energy)
                val elapsed = scheduler.awaitNextFrame(effectiveSpeed)
                
                _elapsedTime.value = elapsed
                
                val pixels = animation.tick(elapsed, brightness, device, energy, bass, mid)
                
                _currentFrame.value = PixelFrame(pixels)
                
                if (glyphManager.isConnected.value) {
                    glyphManager.sendFrame(pixels)
                    if (elapsed % 5000 < 33) {
                        glyphManager.setTimeoutEnabled(false)
                    }
                }
            }
        }
    }

    // Removed modulateWithAudio as it's now internal to the animations

    private fun stopJobOnly() {
        animationJob?.cancel()
        animationJob = null
    }

    fun stop() {
        stopJobOnly()
        isPlaying = false
        currentAnimationName = null
        _currentFrame.value = null
        scheduler.reset()
        glyphManager.clear()
    }

    fun pause() {
        stopJobOnly()
        isPlaying = false
    }

    fun release() {
        stop()
        playerScope.cancel()
        playerScope = CoroutineScope(Dispatchers.Default)
    }
}
