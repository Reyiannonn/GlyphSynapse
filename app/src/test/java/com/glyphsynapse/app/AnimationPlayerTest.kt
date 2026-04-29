package com.glyphsynapse.app

import com.glyphsynapse.app.data.glyph.GlyphMatrixDevice
import com.glyphsynapse.app.data.glyph.GlyphManagerWrapper
import com.glyphsynapse.app.domain.animation.BreatheAnimation
import com.glyphsynapse.app.domain.engine.AnimationPlayer
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class AnimationPlayerTest {

    private lateinit var glyphManager: GlyphManagerWrapper
    private lateinit var player: AnimationPlayer

    @BeforeEach
    fun setup() {
        glyphManager = mockk(relaxed = true)
        every { glyphManager.isConnected } returns MutableStateFlow(true)
        every { glyphManager.device } returns GlyphMatrixDevice.Phone3
        player = AnimationPlayer(glyphManager)
    }

    @Test
    fun `isPlaying becomes true when play is called`() = runTest {
        player.play(BreatheAnimation, 1f, 0.8f, GlyphMatrixDevice.Phone3)
        assertTrue(player.isPlaying)
    }

    @Test
    fun `isPlaying becomes false after stop`() {
        player.play(BreatheAnimation, 1f, 0.8f, GlyphMatrixDevice.Phone3)
        player.stop()
        assertFalse(player.isPlaying)
    }

    @Test
    fun `stop calls clear on glyph manager`() {
        player.play(BreatheAnimation, 1f, 0.8f, GlyphMatrixDevice.Phone3)
        player.stop()
        verify { glyphManager.clear() }
    }

    @Test
    fun `release stops and cleans up`() {
        player.play(BreatheAnimation, 1f, 0.8f, GlyphMatrixDevice.Phone3)
        player.release()
        assertFalse(player.isPlaying)
    }
}
