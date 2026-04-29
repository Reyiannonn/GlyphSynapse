package com.glyphsynapse.app.domain.animation

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import com.glyphsynapse.app.data.glyph.GlyphMatrixDevice
import kotlin.math.sin

/**
 * Premium Charging Fill.
 * Fills from the bottom based on battery %. 
 * The top edge has a moving "liquid" shimmer.
 */
class ChargingFillAnimation(private val context: Context) : AnimationDefinition {
    override val name = "Charging Fill"
    override val description = "Liquid battery fill"

    private val batteryPct: Int
        get() {
            val intent = context.registerReceiver(null, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
            val level = intent?.getIntExtra(BatteryManager.EXTRA_LEVEL, 50) ?: 50
            val scale = intent?.getIntExtra(BatteryManager.EXTRA_SCALE, 100) ?: 100
            return if (scale > 0) (level * 100 / scale) else 50
        }

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
        val pct = batteryPct

        if (pct >= 99) {
            val phase = (elapsedMs % 3000.0) / 3000.0 * TWO_PI
            val v = pixel(0.4f + 0.6f * sin01(phase), brightness + audioEnergy * 0.2f)
            return IntArray(device.matrixSize) { v }
        }

        val fillHeight = (pct / 100f) * h
        val t = elapsedMs / 1000.0

        return IntArray(device.matrixSize) { i ->
            val col = i % w
            val row = i / w
            val rowFromBottom = h - 1 - row
            
            // Wavy top edge - reacts to BASS
            val wave = sin(t * (4.0 + audioBass * 4.0) + col * 0.5) * (0.2 + audioBass * 0.3)
            val currentFill = fillHeight + wave
            
            val dist = rowFromBottom - currentFill
            
            val lit = when {
                dist < -0.5 -> 0.8f // Solid fill
                dist < 0.5 -> {
                    // Anti-aliased edge with shimmer - reacts to MIDS
                    val edge = (1f - (dist + 0.5f)).toFloat()
                    edge * (0.7f + 0.3f * sin01(t * TWO_PI * (1.0 + audioMid)))
                }
                else -> 0f
            }
            
            pixel(lit, brightness)
        }
    }
}
