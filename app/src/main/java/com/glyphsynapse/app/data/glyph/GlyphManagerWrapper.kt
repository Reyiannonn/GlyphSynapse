package com.glyphsynapse.app.data.glyph

import android.content.ComponentName
import android.content.Context
import com.nothing.ketchum.GlyphMatrixFrame
import com.nothing.ketchum.GlyphMatrixManager
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GlyphManagerWrapper @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private var manager: GlyphMatrixManager? = null

    private val _isConnected = MutableStateFlow(false)
    val isConnected: StateFlow<Boolean> = _isConnected

    // Diagnostic steps — visible in the debug panel
    private val _initCalled = MutableStateFlow(false)
    val initCalled: StateFlow<Boolean> = _initCalled

    private val _callbackFired = MutableStateFlow(false)
    val callbackFired: StateFlow<Boolean> = _callbackFired

    private val _registerResult = MutableStateFlow("not called")
    val registerResult: StateFlow<String> = _registerResult

    // Callback to invoke once the service connects, so the animation can (re-)start
    var onConnected: (() -> Unit)? = null

    val device: GlyphMatrixDevice by lazy { GlyphMatrixDevice.detect() }
    val isCompatibleDevice: Boolean get() = device != GlyphMatrixDevice.Stub

    private val callback = object : GlyphMatrixManager.Callback {
        override fun onServiceConnected(componentName: ComponentName?) {
            _callbackFired.value = true
            Timber.d("GlyphMatrix service connected, device=${device.displayName} code=${device.deviceCode}")
            runCatching {
                val mgr = manager
                if (mgr == null) {
                    _registerResult.value = "error: manager is null"
                    return
                }
                val authorized = mgr.register(device.deviceCode)
                _registerResult.value = authorized.toString()
                Timber.d("GlyphMatrix register(${device.deviceCode}) -> authorized=$authorized")
                _isConnected.value = authorized
                if (authorized) onConnected?.invoke()
            }.onFailure {
                _registerResult.value = "error: ${it.javaClass.simpleName}"
                Timber.e(it, "GlyphMatrix register failed")
                _isConnected.value = false
            }
        }

        override fun onServiceDisconnected(componentName: ComponentName?) {
            Timber.d("GlyphMatrix service disconnected")
            _isConnected.value = false
        }
    }

    fun init() {
        if (_initCalled.value && manager != null) {
            Timber.d("GlyphManagerWrapper.init() already called, skipping")
            return
        }

        _initCalled.value = true
        Timber.d("GlyphManagerWrapper.init() device=${device.displayName} compatible=$isCompatibleDevice")
        
        if (!isCompatibleDevice) {
            Timber.w("Device not supported — GlyphMatrix SDK skipped (model=${android.os.Build.MODEL})")
            return
        }

        runCatching {
            // Ensure we are using the application context
            val appRepo = context.applicationContext
            manager = GlyphMatrixManager.getInstance(appRepo).apply {
                init(callback)
            }
            Timber.d("GlyphMatrixManager.init() executed")
        }.onFailure { 
            Timber.e(it, "GlyphMatrixManager init exception")
            _registerResult.value = "init error: ${it.message}"
        }
    }

    /**
     * Sends a greyscale pixel frame to the matrix.
     * Each value in [pixels] is 0–255 (brightness); converted to 12-bit (0–4095) for the SDK.
     */
    fun sendFrame(pixels: IntArray) {
        if (!_isConnected.value) {
            Timber.v("sendFrame skipped — not connected")
            return
        }
        runCatching {
            // setAppMatrixColors expects 12-bit values (0–4095), not ARGB
            val colors = IntArray(pixels.size) { i ->
                pixels[i].coerceIn(0, 255) * 4095 / 255
            }
            manager?.setAppMatrixFrame(colors)
        }.onFailure { Timber.e(it, "sendFrame failed") }
    }

    fun sendFrame(frame: GlyphMatrixFrame) {
        if (!_isConnected.value) return
        runCatching {
            manager?.setAppMatrixFrame(frame)
        }.onFailure { Timber.e(it, "sendFrame(GlyphMatrixFrame) failed") }
    }

    fun clear() {
        if (!_isConnected.value) return
        runCatching { manager?.closeAppMatrix() }
            .onFailure { Timber.e(it, "clear failed") }
    }

    fun setTimeoutEnabled(enabled: Boolean) {
        if (!_isConnected.value) return
        runCatching { manager?.setGlyphMatrixTimeout(enabled) }
            .onFailure { Timber.e(it, "setTimeoutEnabled failed") }
    }

    fun release() {
        runCatching {
            manager?.closeAppMatrix()
            manager?.unInit()
        }.onFailure { Timber.e(it, "release failed") }
        _isConnected.value = false
        manager = null
    }
}
