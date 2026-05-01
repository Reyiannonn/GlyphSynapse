package com.glyphsynapse.app.domain.animation

import com.glyphsynapse.app.data.glyph.GlyphMatrixDevice
import kotlin.math.exp
import kotlin.math.sqrt

/**
 * A realistic, spatial heartbeat.
 * Two radial waves expand from the center (lub-dub).
 */
object HeartbeatAnimation : AnimationDefinition {
    override val name = "Heartbeat"
    override val description = "Radial lub-dub pulse"

    private const val CYCLE_MS = 2400L
    private const val LUB_START = 0L
    private const val DUB_START = 350L

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
        val cx = (w - 1) / 2f
        val cy = (h - 1) / 2f
        val maxDist = sqrt(cx * cx + cy * cy)

        // Phase is based on elapsedMs which is already speed-integrated.
        val t = elapsedMs % CYCLE_MS
        
        return IntArray(device.matrixSize) { i ->
            val x = i % w
            val y = i / w
            val dx = x - cx
            val dy = y - cy
            val dist = sqrt(dx * dx + dy * dy)
            val normDist = dist / maxDist

            // Bass makes the heartbeat pulse much stronger
            val strength = 1.0f + audioBass * 1.5f
            
            val lub = pulse(t - LUB_START, normDist) * strength
            val dub = pulse(t - DUB_START, normDist) * 0.7f * strength

            pixel(maxOf(lub, dub), brightness + audioEnergy * 0.1f)
        }
    }

    private fun pulse(time: Long, distance: Float): Float {
        if (time < 0) return 0f
        val t = time / 1000f
        val waveFront = t * 1.5f 
        val distToFront = kotlin.math.abs(distance - waveFront)
        val intensity = exp(-t * 4.0f) * exp(-distToFront * 8.0f)
        return intensity.toFloat()
    }
}
