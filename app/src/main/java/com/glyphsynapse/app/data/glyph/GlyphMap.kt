package com.glyphsynapse.app.data.glyph

import com.nothing.ketchum.Common

/**
 * Represents a supported GlyphMatrix device with its LED grid dimensions and SDK device code.
 */
sealed class GlyphMatrixDevice {

    abstract val matrixWidth: Int
    abstract val matrixHeight: Int
    abstract val displayName: String
    abstract val deviceCode: String

    val matrixSize: Int get() = matrixWidth * matrixHeight

    /**
     * Nothing Phone (3) — 25×25 LED matrix, supports touch events.
     * Device code "A024" from SDK static initializer (Glyph.DEVICE_23112).
     * Using literal to avoid NoSuchFieldError if the on-device SDK is older.
     */
    object Phone3 : GlyphMatrixDevice() {
        override val matrixWidth  = 25
        override val matrixHeight = 25
        override val displayName  = "Nothing Phone (3)"
        override val deviceCode   = "A024"
    }

    /**
     * Nothing Phone (4a) Pro — 13×13 LED matrix, AOD-only (no touch).
     * Device code "A069P" from SDK static initializer (Glyph.DEVICE_25111p).
     * Using literal to avoid NoSuchFieldError if the on-device SDK is older.
     */
    object Phone4aPro : GlyphMatrixDevice() {
        override val matrixWidth  = 13
        override val matrixHeight = 13
        override val displayName  = "Nothing Phone (4a) Pro"
        override val deviceCode   = "A069P"
    }

    /** Fallback — lets preview and tests run on unsupported hardware. */
    object Stub : GlyphMatrixDevice() {
        override val matrixWidth  = 25
        override val matrixHeight = 25
        override val displayName  = "Unsupported Device"
        override val deviceCode   = ""
    }

    companion object {
        /**
         * Known model strings and device names from Phone (3) and (4a) Pro.
         */
        private val PHONE3_IDS = setOf("A024", "Metroid")
        private val PHONE4APRO_IDS = setOf("A069P", "Pacman")

        /**
         * Detects device via Build.MODEL and Build.DEVICE first (reliable),
         * then falls back to SDK Common helpers.
         */
        fun detect(): GlyphMatrixDevice {
            val model = android.os.Build.MODEL.trim()
            val deviceName = android.os.Build.DEVICE.trim()

            return when {
                PHONE3_IDS.any { model.contains(it, ignoreCase = true) || deviceName.contains(it, ignoreCase = true) } -> Phone3
                PHONE4APRO_IDS.any { model.contains(it, ignoreCase = true) || deviceName.contains(it, ignoreCase = true) } -> Phone4aPro
                // Secondary: try SDK helpers
                else -> runCatching {
                    when {
                        Common.is23112()  -> Phone3
                        Common.is25111p() -> Phone4aPro
                        else -> Stub
                    }
                }.getOrElse { Stub }
            }
        }

        fun isSupported(): Boolean = detect() != Stub
    }
}

