package com.glyphsynapse.app.domain.animation

import com.glyphsynapse.app.data.glyph.GlyphMatrixDevice
import kotlin.math.sin
import kotlin.math.sqrt

/** 
 * Vitality.
 * A non-linear biological breath with a shifting center.
 */
object BreatheAnimation : AnimationDefinition {
    override val name = "Vitality"
    override val description = "Biological resting breath"

    private const val CYCLE_MS = 5000.0

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
        
        val t = elapsedMs / 1000.0
        
        // Music speeds up the resting heart rate
        val audioSpeedBoost = audioEnergy * 2.0
        
        // The "Soul" of the device drifts slightly over time
        val driftX = (sin(t * 0.13) * 2.0).toFloat()
        val driftY = (sin(t * 0.17) * 2.0).toFloat()
        
        val cx = (w - 1) / 2f + driftX
        val cy = (h - 1) / 2f + driftY
        val maxDist = sqrt(cx * cx + cy * cy)

        // Non-linear bio-rhythm
        val mainPhase = (elapsedMs % CYCLE_MS) / CYCLE_MS * TWO_PI
        val bioPulse = sin(mainPhase + audioSpeedBoost) + 0.15 * sin(mainPhase * 2.3)
        val normalizedPulse = ((bioPulse + 1.15) / 2.3).toFloat()
        val easedPulse = normalizedPulse * normalizedPulse

        return IntArray(device.matrixSize) { i ->
            val x = i % w
            val y = i / w
            val dx = x - cx
            val dy = y - cy
            val dist = sqrt(dx * dx + dy * dy)
            val normDist = dist / maxDist

            // Waves travel out, but with organic lag
            val localPhase = mainPhase - (normDist * 1.5)
            val localInt = (sin(localPhase) + 1.0) / 2.0
            
            val falloff = 1f - (normDist * 0.7f)
            val lit = (localInt.toFloat() * easedPulse * falloff)
            
            pixel(lit, brightness + audioEnergy * 0.2f)
        }
    }
}
