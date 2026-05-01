package com.glyphsynapse.app.domain.animation

import com.glyphsynapse.app.data.glyph.GlyphMatrixDevice
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.sqrt

/** 
 * Premium Orbit.
 */
object OrbitAnimation : AnimationDefinition {
    override val name = "Orbit"
    override val description = "Comet with long fading tail"

    private const val CYCLE_MS = 2200.0
    private const val ORBIT_RADIUS_FRAC = 0.4f

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
        val r = minOf(cx, cy) * ORBIT_RADIUS_FRAC

        // Head position is purely based on elapsedMs for temporal stability.
        val headAngle = (elapsedMs % CYCLE_MS) / CYCLE_MS * TWO_PI

        return IntArray(device.matrixSize) { i ->
            val x = i % w
            val y = i / w
            val dx = x - cx
            val dy = y - cy

            val dist = sqrt(dx * dx + dy * dy)
            val ringDist = abs(dist - r)
            
            val angle = atan2(dy.toDouble(), dx.toDouble())
            val angularDist = (headAngle - angle + TWO_PI) % TWO_PI
            
            // Tail stretches and brightens with music mids
            val tailDecay = 2.5 - (audioMid * 1.5)
            val angularFactor = kotlin.math.exp(-angularDist * tailDecay).toFloat()
            val ringFactor = kotlin.math.exp(-ringDist * 1.8).toFloat()
            
            val lit = angularFactor * ringFactor
            pixel(lit, brightness + audioEnergy * 0.15f)
        }
    }
}
