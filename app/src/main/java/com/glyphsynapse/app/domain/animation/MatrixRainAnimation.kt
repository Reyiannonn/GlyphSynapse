package com.glyphsynapse.app.domain.animation

import com.glyphsynapse.app.data.glyph.GlyphMatrixDevice
import kotlin.random.Random

/**
 * Premium Digital Rain.
 * Optimized per-column simulation with smooth temporal stability.
 */
object MatrixRainAnimation : AnimationDefinition {
    override val name = "Matrix Rain"
    override val description = "Glimmering digital streaks"

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

        // Note: speedFactor is removed here because speed is integrated in elapsedMs
        val t = elapsedMs / 1000.0f

        for (col in 0 until w) {
            // Deterministic seed per column
            val colSeed = col.toLong() * 12345L
            val rand = Random(colSeed)
            
            val fallSpeed = (0.5f + rand.nextFloat() * 1.5f)
            val delay = rand.nextFloat() * 5.0f
            val cycleTime = 4.0f
            
            // Linear progress calculation based on time
            val progress = ((t * fallSpeed + delay) % cycleTime) / cycleTime
            val headY = progress * (h + 10) - 5
            
            val tailLength = 4f + rand.nextFloat() * 6f
            
            for (row in 0 until h) {
                val dist = headY - row
                if (dist < 0 || dist > tailLength) continue
                
                // Smooth head with sharp tip
                var lit = if (dist < 0.5f) {
                    (dist + 0.5f) // Sharp entry
                } else {
                    1.0f - (dist / tailLength) // Fading tail
                }
                
                // Digital flicker/glimmer
                val pixelSeed = colSeed + row * 987L + (elapsedMs / 60)
                val flickerRand = Random(pixelSeed).nextFloat()
                val flickerDepth = 0.25f + (audioMid * 0.5f)
                val flicker = (1.0f - flickerDepth) + (flickerRand * flickerDepth)
                
                lit *= flicker
                
                // Head is slightly brighter
                if (dist < 1.0f) lit *= (1.2f + audioEnergy * 0.5f)

                pixels[idx(col, row, w)] = pixel(lit * lit, brightness)
            }
        }

        return pixels
    }
}
