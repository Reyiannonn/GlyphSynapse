package com.glyphsynapse.app.domain.animation

import com.glyphsynapse.app.data.glyph.GlyphMatrixDevice
import kotlin.math.sin
import kotlin.random.Random

/**
 * Presence.
 * Deep-space organic drift with rare, long-lived stochastic neural sparkles.
 */
object IdleDriftAnimation : AnimationDefinition {
    override val name = "Presence"
    override val description = "Deep organic neural standby"

    private const val SPARKLE_DURATION = 1400L // 1.4 seconds per sparkle
    private const val SPARKLE_CHANCE = 0.0012f  // base rarity

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
        
        // Music expands the background perception
        val cap = (brightness + audioBass * 0.3f) * 0.15f

        val t = elapsedMs / 1000.0

        return IntArray(device.matrixSize) { i ->
            val x = (i % w).toFloat() / w
            val y = (i / w).toFloat() / h
            
            // 1. Layered "Cloud" Noise (The Drift)
            // Fix: Constant drift speed inside sin() prevents jumping.
            // Acceleration is handled globally via 't' in AnimationPlayer.
            val layer1 = sin(t * 0.10 + x * 1.5 + y * 1.2)
            val layer2 = sin(t * 0.18 - x * 1.1 + y * 1.8)
            val combined = (layer1 * 0.6 + layer2 * 0.4)
            
            var lit = (combined - 0.25).coerceAtLeast(0.0).toFloat()
            lit = lit * lit * lit 
            
            // 2. Neural Sparkles (Stochastic & Desynchronised)
            // Sparkles react to MIDS (vocals/instruments)
            val sparkleChance = SPARKLE_CHANCE * (1f + audioMid * 12f)
            
            val pixelOffset = (i * 739L) % SPARKLE_DURATION
            val localTime = elapsedMs + pixelOffset
            val timeBucket = localTime / SPARKLE_DURATION
            
            val seed = (i.toLong() * 31337L) xor (timeBucket * 6271L)
            val rand = Random(seed)
            
            if (rand.nextFloat() < sparkleChance) {
                val progress = (localTime % SPARKLE_DURATION) / SPARKLE_DURATION.toFloat()
                val sparkleIntensity = sin(progress * Math.PI).toFloat()
                
                // Sparkles pulse harder with the mids
                val power = 0.7f + (audioMid * 0.8f)
                lit = (lit + (sparkleIntensity * power)).coerceIn(0f, 1f)
            }
            
            pixel(lit, cap)
        }
    }
}
