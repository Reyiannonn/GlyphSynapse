package com.glyphsynapse.app.domain.model

/**
 * A wrapper for the pixel array to ensure structural equality
 * and prevent unnecessary UI recompositions.
 */
data class PixelFrame(
    val pixels: IntArray
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        return pixels.contentEquals((other as PixelFrame).pixels)
    }

    override fun hashCode(): Int = pixels.contentHashCode()
}
