package com.glyphsynapse.app.ui.theme

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color

/**
 * Fallback dot-matrix heading renderer for when Ndot-55 font is unavailable.
 * Each character is drawn as a 5×7 dot grid on Compose Canvas.
 */
@Composable
fun DotMatrixText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = TextPrimary,
    dotSizePx: Float = 6f,
    gapPx: Float = 3f
) {
    Canvas(modifier = modifier) {
        var xCursor = 0f
        for (char in text.uppercase()) {
            val bitmap = FONT_5X7[char] ?: FONT_5X7[' ']!!
            for (row in 0..6) {
                for (col in 0..4) {
                    if (bitmap[row] and (1 shl (4 - col)) != 0) {
                        drawRoundRect(
                            color = color,
                            topLeft = Offset(xCursor + col * (dotSizePx + gapPx), row * (dotSizePx + gapPx)),
                            size = Size(dotSizePx, dotSizePx),
                            cornerRadius = CornerRadius(1f, 1f)
                        )
                    }
                }
            }
            xCursor += 5 * (dotSizePx + gapPx) + gapPx * 2
        }
    }
}

// 5×7 dot-matrix font bitmaps — each row is a 5-bit mask (bit4=leftmost)
private val FONT_5X7: Map<Char, IntArray> = mapOf(
    ' ' to intArrayOf(0b00000, 0b00000, 0b00000, 0b00000, 0b00000, 0b00000, 0b00000),
    'A' to intArrayOf(0b01110, 0b10001, 0b10001, 0b11111, 0b10001, 0b10001, 0b10001),
    'B' to intArrayOf(0b11110, 0b10001, 0b10001, 0b11110, 0b10001, 0b10001, 0b11110),
    'C' to intArrayOf(0b01110, 0b10001, 0b10000, 0b10000, 0b10000, 0b10001, 0b01110),
    'D' to intArrayOf(0b11100, 0b10010, 0b10001, 0b10001, 0b10001, 0b10010, 0b11100),
    'E' to intArrayOf(0b11111, 0b10000, 0b10000, 0b11110, 0b10000, 0b10000, 0b11111),
    'F' to intArrayOf(0b11111, 0b10000, 0b10000, 0b11110, 0b10000, 0b10000, 0b10000),
    'G' to intArrayOf(0b01110, 0b10001, 0b10000, 0b10111, 0b10001, 0b10001, 0b01111),
    'H' to intArrayOf(0b10001, 0b10001, 0b10001, 0b11111, 0b10001, 0b10001, 0b10001),
    'I' to intArrayOf(0b01110, 0b00100, 0b00100, 0b00100, 0b00100, 0b00100, 0b01110),
    'J' to intArrayOf(0b00111, 0b00010, 0b00010, 0b00010, 0b10010, 0b10010, 0b01100),
    'K' to intArrayOf(0b10001, 0b10010, 0b10100, 0b11000, 0b10100, 0b10010, 0b10001),
    'L' to intArrayOf(0b10000, 0b10000, 0b10000, 0b10000, 0b10000, 0b10000, 0b11111),
    'M' to intArrayOf(0b10001, 0b11011, 0b10101, 0b10001, 0b10001, 0b10001, 0b10001),
    'N' to intArrayOf(0b10001, 0b11001, 0b10101, 0b10011, 0b10001, 0b10001, 0b10001),
    'O' to intArrayOf(0b01110, 0b10001, 0b10001, 0b10001, 0b10001, 0b10001, 0b01110),
    'P' to intArrayOf(0b11110, 0b10001, 0b10001, 0b11110, 0b10000, 0b10000, 0b10000),
    'Q' to intArrayOf(0b01110, 0b10001, 0b10001, 0b10001, 0b10101, 0b10010, 0b01101),
    'R' to intArrayOf(0b11110, 0b10001, 0b10001, 0b11110, 0b10100, 0b10010, 0b10001),
    'S' to intArrayOf(0b01111, 0b10000, 0b10000, 0b01110, 0b00001, 0b00001, 0b11110),
    'T' to intArrayOf(0b11111, 0b00100, 0b00100, 0b00100, 0b00100, 0b00100, 0b00100),
    'U' to intArrayOf(0b10001, 0b10001, 0b10001, 0b10001, 0b10001, 0b10001, 0b01110),
    'V' to intArrayOf(0b10001, 0b10001, 0b10001, 0b10001, 0b01010, 0b01010, 0b00100),
    'W' to intArrayOf(0b10001, 0b10001, 0b10001, 0b10101, 0b10101, 0b11011, 0b10001),
    'X' to intArrayOf(0b10001, 0b01010, 0b00100, 0b00100, 0b00100, 0b01010, 0b10001),
    'Y' to intArrayOf(0b10001, 0b01010, 0b00100, 0b00100, 0b00100, 0b00100, 0b00100),
    'Z' to intArrayOf(0b11111, 0b00001, 0b00010, 0b00100, 0b01000, 0b10000, 0b11111),
    '0' to intArrayOf(0b01110, 0b10011, 0b10101, 0b10101, 0b11001, 0b10001, 0b01110),
    '1' to intArrayOf(0b00100, 0b01100, 0b00100, 0b00100, 0b00100, 0b00100, 0b01110),
    '2' to intArrayOf(0b01110, 0b10001, 0b00001, 0b00010, 0b00100, 0b01000, 0b11111),
    '3' to intArrayOf(0b11111, 0b00010, 0b00100, 0b00110, 0b00001, 0b10001, 0b01110),
    '4' to intArrayOf(0b00010, 0b00110, 0b01010, 0b10010, 0b11111, 0b00010, 0b00010),
    '5' to intArrayOf(0b11111, 0b10000, 0b11110, 0b00001, 0b00001, 0b10001, 0b01110),
    '6' to intArrayOf(0b01110, 0b10001, 0b10000, 0b11110, 0b10001, 0b10001, 0b01110),
    '7' to intArrayOf(0b11111, 0b00001, 0b00010, 0b00100, 0b00100, 0b00100, 0b00100),
    '8' to intArrayOf(0b01110, 0b10001, 0b10001, 0b01110, 0b10001, 0b10001, 0b01110),
    '9' to intArrayOf(0b01110, 0b10001, 0b10001, 0b01111, 0b00001, 0b10001, 0b01110),
    '.' to intArrayOf(0b00000, 0b00000, 0b00000, 0b00000, 0b00000, 0b00000, 0b00100),
    '-' to intArrayOf(0b00000, 0b00000, 0b00000, 0b11111, 0b00000, 0b00000, 0b00000),
)
