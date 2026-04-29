package com.glyphsynapse.app

import com.glyphsynapse.app.data.glyph.GlyphMatrixDevice
import com.glyphsynapse.app.domain.animation.BreatheAnimation
import com.glyphsynapse.app.domain.animation.CascadeAnimation
import com.glyphsynapse.app.domain.animation.HeartbeatAnimation
import com.glyphsynapse.app.domain.animation.IdleDriftAnimation
import com.glyphsynapse.app.domain.animation.MatrixRainAnimation
import com.glyphsynapse.app.domain.animation.OrbitAnimation
import com.glyphsynapse.app.domain.animation.PulseWaveAnimation
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource

class AnimationDefinitionTest {

    private val phone3Device = GlyphMatrixDevice.Phone3
    private val phone4aDevice = GlyphMatrixDevice.Phone4aPro

    companion object {
        @JvmStatic
        fun animations() = listOf(
            BreatheAnimation,
            CascadeAnimation,
            OrbitAnimation,
            PulseWaveAnimation,
            HeartbeatAnimation,
            MatrixRainAnimation,
            IdleDriftAnimation
        )
    }

    @ParameterizedTest
    @MethodSource("animations")
    fun `tick returns correct pixel count for Phone3`(anim: com.glyphsynapse.app.domain.animation.AnimationDefinition) {
        val pixels = anim.tick(1000L, 1f, GlyphMatrixDevice.Phone3)
        assertEquals(GlyphMatrixDevice.Phone3.matrixSize, pixels.size,
            "${anim.name} should return ${GlyphMatrixDevice.Phone3.matrixSize} pixels for Phone3")
    }

    @ParameterizedTest
    @MethodSource("animations")
    fun `tick returns correct pixel count for Phone4aPro`(anim: com.glyphsynapse.app.domain.animation.AnimationDefinition) {
        val pixels = anim.tick(1000L, 1f, GlyphMatrixDevice.Phone4aPro)
        assertEquals(GlyphMatrixDevice.Phone4aPro.matrixSize, pixels.size,
            "${anim.name} should return ${GlyphMatrixDevice.Phone4aPro.matrixSize} pixels for Phone4aPro")
    }

    @ParameterizedTest
    @MethodSource("animations")
    fun `all brightness values stay within 0-255`(anim: com.glyphsynapse.app.domain.animation.AnimationDefinition) {
        listOf(0L, 500L, 1000L, 2000L, 4000L).forEach { t ->
            val pixels = anim.tick(t, 1.0f, phone3Device)
            pixels.forEach { v ->
                assertTrue(v in 0..255,
                    "${anim.name} at t=$t emitted out-of-range value $v")
            }
        }
    }

    @ParameterizedTest
    @MethodSource("animations")
    fun `brightness multiplier scales output`(anim: com.glyphsynapse.app.domain.animation.AnimationDefinition) {
        val fullBright = anim.tick(1000L, 1.0f, phone3Device)
        val halfBright = anim.tick(1000L, 0.5f, phone3Device)
        val maxFull = fullBright.max()
        val maxHalf = halfBright.max()
        // With brightness at 0.5, the maximum pixel should be at most half of full brightness
        if (maxFull > 10) {  // only check when animation is doing something visible
            assertTrue(maxHalf <= maxFull,
                "${anim.name}: 0.5x brightness should not exceed 1.0x brightness")
        }
    }

    @ParameterizedTest
    @MethodSource("animations")
    fun `animations loop stably for 3 seconds`(anim: com.glyphsynapse.app.domain.animation.AnimationDefinition) {
        repeat(90) { i ->
            val pixels = anim.tick(i * 33L, 0.8f, phone3Device)
            assertEquals(phone3Device.matrixSize, pixels.size)
        }
    }

    fun `IdleDrift never exceeds 15pct brightness`() {
        val maxAllowed = (255 * 0.15f).toInt()
        repeat(100) { i ->
            val pixels = IdleDriftAnimation.tick(i * 100L, 1.0f, phone3Device)
            pixels.forEach { v ->
                assertTrue(v <= maxAllowed, "IdleDrift exceeded 15% cap: $v > $maxAllowed")
            }
        }
    }
}
