package com.glyphsynapse.app.domain.animation

import com.glyphsynapse.app.data.glyph.GlyphMatrixDevice
import kotlin.math.abs

/** 
 * Liquid Cascade.
 */
object CascadeAnimation : AnimationDefinition {
    override val name = "Cascade"
    override val description = "Liquid vertical sweep"

    private const val CYCLE_MS = 2500.0
    private const val TAIL_WIDTH = 0.4f

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
        
        // Bass accelerates the flow
        val speedBoost = audioBass * 1.5
        val progress = (((elapsedMs * (1.0 + speedBoost)) % CYCLE_MS) / CYCLE_MS).toFloat()
        
        val cx = (w - 1) / 2f

        return IntArray(device.matrixSize) { i ->
            val col = i % w
            val row = i / w
            
            val horizontalOffset = abs(col - cx) / cx * 0.15f
            val rowPos = (row.toFloat() / (h - 1).coerceAtLeast(1)) + horizontalOffset
            
            val dist = ((progress * 1.5f - rowPos + 1f) % 1f)
            val lit = if (dist < TAIL_WIDTH) {
                val t = 1f - dist / TAIL_WIDTH
                // Mids make the liquid shimmer
                val shimmer = 1f + (audioMid * 0.5f * kotlin.math.sin(elapsedMs * 0.02 + i))
                (t * t * t * shimmer).coerceIn(0.0, 1.5).toFloat()
            } else 0f

            pixel(lit, brightness)
        }
    }
}
