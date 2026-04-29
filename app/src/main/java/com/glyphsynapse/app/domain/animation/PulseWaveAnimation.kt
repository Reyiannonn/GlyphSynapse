package com.glyphsynapse.app.domain.animation

import com.glyphsynapse.app.data.glyph.GlyphMatrixDevice
import kotlin.math.abs

import kotlin.math.exp
import kotlin.math.sqrt

/** 
 * Synapse.
 */
object PulseWaveAnimation : AnimationDefinition {
    override val name = "Synapse"
    override val description = "Neural signal pulses"

    private const val CYCLE_MS = 3200L

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
        
        // Use elapsedMs directly; AnimationPlayer handles music-based acceleration smoothly
        val t = elapsedMs % CYCLE_MS
        
        val nodes = listOf(
            Node(0.3f, 0.3f, 0L),
            Node(0.7f, 0.5f, 1000L),
            Node(0.4f, 0.8f, 2000L)
        )

        return IntArray(device.matrixSize) { i ->
            val x = (i % w).toFloat() / (w - 1)
            val y = (i / w).toFloat() / (h - 1)
            
            var totalLit = 0f
            for (node in nodes) {
                val dt = t - node.startTime
                if (dt < 0) continue
                
                val progress = (dt / 1200f).toFloat()
                if (progress > 1f) continue
                
                val dx = x - node.x
                val dy = y - node.y
                val dist = sqrt((dx * dx + dy * dy).toDouble())
                
                val waveFront = progress * 1.2f
                // Mids tighten the synapse connections (sharper rings)
                val sharpness = 12.0f + (audioMid * 10f)
                val ring = exp(-abs(dist - waveFront) * sharpness).toFloat()
                val fade = 1f - progress
                
                totalLit = maxOf(totalLit, ring * fade)
            }

            pixel(totalLit, brightness + audioBass * 0.3f)
        }
    }

    private data class Node(val x: Float, val y: Float, val startTime: Long)
}
