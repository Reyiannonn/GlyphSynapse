package com.glyphsynapse.app.domain.animation

import com.glyphsynapse.app.data.glyph.GlyphMatrixDevice
import kotlin.math.abs
import kotlin.math.exp
import kotlin.math.sin

/** 
 * Liquid Cascade.
 * Features a center-leading V-shape and extended run-off to ensure clean looping.
 */
object CascadeAnimation : AnimationDefinition {
    override val name = "Cascade"
    override val description = "Liquid vertical sweep"

    // Slightly faster cycle to compensate for longer travel distance
    private const val CYCLE_MS = 1800.0

    override fun tick(
        elapsedMs: Long, 
        brightness: Float, 
        device: GlyphMatrixDevice,
        audioEnergy: Float,
        audioBass: Float,
        audioMid: Float
    ): IntArray {
        val w = device.matrixWidth
        val h = device.matrixHeight
        val pixels = IntArray(device.matrixSize)
        
        val progress = (elapsedMs % CYCLE_MS.toLong()) / CYCLE_MS.toFloat()
        val cx = (w - 1) / 2f

        for (i in 0 until device.matrixSize) {
            val col = i % w
            val row = i / w
            
            val nx = abs(col - cx) / cx
            val ny = row.toFloat() / (h - 1).coerceAtLeast(1)
            
            // Extended range (2.4f) ensures the tail (0.8f length) fully clears
            // the bottom (1.0f) before the head resets at the top.
            val headPos = progress * 2.4f - 0.7f
            
            // V-Shape: Center (nx=0) leads. As nx increases, headPos effectively "decreases" 
            // for that pixel, making the edges lag behind the center.
            val dist = (headPos - ny) - (nx * 0.15f)
            
            var lit = 0f
            if (dist > 0 && dist < 0.8f) {
                // Sharper exponential decay for a cleaner liquid tail
                lit = exp(-dist * 10.0f)
                
                // Leading edge shimmer
                if (dist < 0.1f) {
                    val shimmer = 1.0f + (audioMid * 0.7f * sin(elapsedMs * 0.03 + i * 0.15)).toFloat()
                    lit *= shimmer
                }
            }
            
            val ambient = audioEnergy * 0.03f
            pixels[i] = pixel(lit + ambient, brightness)
        }

        return pixels
    }
}
