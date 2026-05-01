package com.glyphsynapse.app.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import com.glyphsynapse.app.data.glyph.GlyphMatrixDevice
import com.glyphsynapse.app.domain.animation.AnimationDefinition
import com.glyphsynapse.app.ui.theme.Background
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlin.math.sqrt

/**
 * Live back-panel preview rendered as a circle (matching the physical Glyph Matrix shape).
 * LEDs outside the inscribed circle of the matrix are dimmed to match real hardware masking.
 *
 * @param mini  compact thumbnail variant (used in animation cards)
 */
@Composable
fun GlyphPreview(
    animation: AnimationDefinition,
    device: GlyphMatrixDevice,
    speedMultiplier: Float = 1f,
    brightness: Float = 0.8f,
    modifier: Modifier = Modifier,
    mini: Boolean = false,
    externalPixels: IntArray? = null
) {
    var elapsed by remember { mutableLongStateOf(0L) }

    // Only run internal timer if no external pixels are provided
    LaunchedEffect(animation, speedMultiplier, externalPixels) {
        if (externalPixels != null) return@LaunchedEffect
        var lastTick = System.currentTimeMillis()
        while (isActive) {
            // Lower FPS for preview to save CPU
            delay(40)
            val now = System.currentTimeMillis()
            elapsed += ((now - lastTick) * speedMultiplier).toLong()
            lastTick = now
        }
    }

    val pixels = remember(animation, elapsed, brightness, device, externalPixels) {
        externalPixels ?: animation.tick(elapsed, brightness, device)
    }

    val inCircleMask = remember(device) {
        val w = device.matrixWidth
        val h = device.matrixHeight
        val cx = w / 2f
        val cy = h / 2f
        val radius = (w.coerceAtMost(h) / 2f)
        BooleanArray(w * h) { i ->
            val x = i % w
            val y = i / w
            val dx = x + 0.5f - cx
            val dy = y + 0.5f - cy
            sqrt(dx * dx + dy * dy) <= radius
        }
    }

    Box(
        modifier = modifier
            .aspectRatio(1f)
            .clip(CircleShape)
            .background(Background)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = device.matrixWidth
            val h = device.matrixHeight
            val cellW = size.width / w
            val cellH = size.height / h
            val dotW = cellW * 0.78f
            val dotH = cellH * 0.78f
            val padX = (cellW - dotW) / 2f
            val padY = (cellH - dotH) / 2f

            for (i in pixels.indices) {
                val v = pixels[i].coerceIn(0, 255)
                if (v <= 0) continue

                val inCircle = inCircleMask[i]
                val alpha = if (inCircle) {
                    (v / 255f).coerceAtLeast(0.04f)
                } else {
                    (v / 255f) * 0.08f
                }

                if (alpha > 0.01f) {
                    val col = i % w
                    val row = i / w
                    drawRect(
                        color = Color.White.copy(alpha = alpha),
                        topLeft = Offset(col * cellW + padX, row * cellH + padY),
                        size = Size(dotW, dotH)
                    )
                }
            }
        }
    }
}
