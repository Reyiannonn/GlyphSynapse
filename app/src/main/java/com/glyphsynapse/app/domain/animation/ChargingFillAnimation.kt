package com.glyphsynapse.app.domain.animation

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import com.glyphsynapse.app.data.glyph.GlyphMatrixDevice
import kotlin.math.sin
import kotlin.random.Random

/**
 * Premium Charging Fill.
 * Fills from the bottom based on battery %. 
 * When music plays, the surface "splashes" like a speaker in water.
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
        val pixels = IntArray(device.matrixSize)

        // Handle full battery with a slow "breathing" glow
        if (pct >= 99) {
            val phase = (elapsedMs % 4000.0) / 4000.0 * TWO_PI
            val intensity = 0.5f + 0.5f * sin(phase).toFloat()
            val v = pixel(intensity, brightness + audioEnergy * 0.2f)
            return IntArray(device.matrixSize) { v }
        }

        val fillHeight = (pct / 100f) * h
        val t = elapsedMs / 1000.0
        
        // Audio-driven turbulence factors
        val splashHeight = audioBass * 3.5f
        val sprayIntensity = audioEnergy * 1.5f
        // Slower ripples for a "heavier" liquid feel
        val rippleSpeed = 2.0 + audioBass * 4.0

        for (i in 0 until device.matrixSize) {
            val col = i % w
            val row = i / w
            val rowFromBottom = h - 1 - row
            
            // 1. Turbulent Surface Wave
            // Complex wave: sum of two sines for more "organic" liquid movement
            val wave1 = sin(t * rippleSpeed + col * 0.6) * (0.2 + audioBass * 0.5)
            val wave2 = sin(t * (rippleSpeed * 0.7) - col * 0.3) * (0.15 + audioBass * 0.3)
            val currentSurface = fillHeight + wave1 + wave2
            
            val distToSurface = rowFromBottom - currentSurface
            
            var lit = 0f
            
            if (distToSurface < 0) {
                // BELOW SURFACE: Solid fill with "submerged" vibration
                // Higher audioMid makes the submerged liquid shimmer
                val shimmer = 0.8f + 0.2f * sin(t * 30.0 + i * 0.5).toFloat() * audioMid
                lit = shimmer.coerceIn(0.7f, 1.0f)
            } else if (distToSurface < 0.6) {
                // SURFACE EDGE: Anti-aliased line
                lit = (1.0 - distToSurface / 0.6).toFloat()
            } else {
                // ABOVE SURFACE: Splash particles (Spray)
                // Deterministic flicker based on position and time
                // Slower refresh (100ms) makes droplets "hang" in the air
                val particleSeed = i.toLong() * 91L + (elapsedMs / 100)
                val rand = Random(particleSeed)
                
                // Chance of a splash pixel decreases with distance from surface
                val splashThreshold = 1.0 - (distToSurface / (1.5 + splashHeight))
                if (rand.nextFloat() < (splashThreshold * sprayIntensity.toDouble() * 0.4).coerceAtLeast(0.0)) {
                    // Softer brightness range to avoid digital harshness
                    lit = 0.4f + rand.nextFloat() * 0.5f
                }
            }
            
            pixels[i] = pixel(lit, brightness)
        }

        return pixels
    }
}
