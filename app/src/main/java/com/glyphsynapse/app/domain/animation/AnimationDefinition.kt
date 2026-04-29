package com.glyphsynapse.app.domain.animation

import com.glyphsynapse.app.data.glyph.GlyphMatrixDevice
import kotlin.math.PI

/**
 * A single animation scene.
 *
 * Each animation renders to a greyscale pixel array sized [device.matrixSize].
 * Values are 0..255 (LED off to full brightness), scaled by the master brightness.
 */
interface AnimationDefinition {
    val name: String
    val description: String

    /**
     * Returns the full pixel frame for the given instant.
     *
     * @param elapsedMs  time since animation start, pre-scaled for speed
     * @param brightness overall brightness multiplier 0.0..1.0
     * @param device     target device (determines matrix dimensions)
     * @param audioEnergy current audio level 0.0..1.0 (optional integration)
     * @return           IntArray of size [device.matrixSize], each value 0..255
     */
    fun tick(
        elapsedMs: Long, 
        brightness: Float, 
        device: GlyphMatrixDevice,
        audioEnergy: Float = 0f,
        audioBass: Float = 0f,
        audioMid: Float = 0f
    ): IntArray

    /** Representative static frames used in the preview / thumbnail (no timing needed). */
    fun previewFrames(device: GlyphMatrixDevice): List<IntArray> =
        (0..5).map { i -> tick(i * 500L, 0.8f, device) }
}

// ── Shared helpers ─────────────────────────────────────────────────────────

/** Maps a 0..1 normalised brightness to a 0..255 int, clamped and scaled by master brightness. */
internal fun pixel(normalised: Float, masterBrightness: Float): Int =
    (normalised.coerceIn(0f, 1f) * masterBrightness * 255f).toInt()

/** Returns a value in 0..1 by mapping sin(radians) from -1..1 to 0..1. */
internal fun sin01(radians: Double): Float = ((Math.sin(radians) + 1.0) / 2.0).toFloat()

internal const val TWO_PI = 2.0 * PI

/** Returns the flat-array index for pixel (x, y) in a matrix of the given width. */
internal fun idx(x: Int, y: Int, width: Int) = y * width + x
