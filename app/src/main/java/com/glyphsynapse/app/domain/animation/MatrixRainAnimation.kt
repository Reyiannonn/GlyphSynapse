package com.glyphsynapse.app.domain.animation

import com.glyphsynapse.app.data.glyph.GlyphMatrixDevice
import kotlin.random.Random

/**
 * Premium Digital Rain.
 */
object MatrixRainAnimation : AnimationDefinition {
    override val name = "Matrix Rain"
    override val description = "Glimmering digital streaks"

    private const val DROP_SPEED_MS = 100.0

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

        // Bass makes the rain faster
        val speedFactor = 1.0 + (audioBass * 1.5)
        val adjustedElapsed = (elapsedMs * speedFactor).toLong()

        for (col in 0 until w) {
            val colSeed = col.toLong() * 9123L
            val phaseOffset = Random(colSeed).nextLong(0L, 5000L)
            val t = (adjustedElapsed + phaseOffset) % 4000L

            val headRow = (t / DROP_SPEED_MS).toInt() % (h + 12) - 6
            val tailLength = 5 + (Random(colSeed).nextInt(5))

            for (row in 0 until h) {
                val dist = headRow - row
                if (dist < 0 || dist > tailLength) continue
                
                var lit = 1f - dist.toFloat() / tailLength
                
                // Mids make the flicker much more intense
                if (dist > 0) {
                    val pixelSeed = colSeed + row * 31L + (elapsedMs / 40)
                    val flickerDepth = 0.5f + (audioMid * 0.5f)
                    val flicker = (1f - flickerDepth) + flickerDepth * Random(pixelSeed).nextFloat()
                    lit *= flicker
                }
                
                pixels[idx(col, row, w)] = pixel(lit * lit, brightness + audioEnergy * 0.1f)
            }
        }

        return pixels
    }
}
