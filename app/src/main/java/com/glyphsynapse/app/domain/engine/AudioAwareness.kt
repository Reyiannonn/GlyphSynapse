package com.glyphsynapse.app.domain.engine

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.media.audiofx.Visualizer
import androidx.core.content.ContextCompat
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.sqrt

/**
 * Monitors system audio and extracts frequency data (BASS, MID, HIGH).
 */
@Singleton
class AudioAwareness @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private var visualizer: Visualizer? = null
    
    private val _energy = MutableStateFlow(0f)
    val energy: StateFlow<Float> = _energy.asStateFlow()

    private val _bass = MutableStateFlow(0f)
    val bass: StateFlow<Float> = _bass.asStateFlow()

    private val _mid = MutableStateFlow(0f)
    val mid: StateFlow<Float> = _mid.asStateFlow()

    private val _isActive = MutableStateFlow(false)
    val isActive: StateFlow<Boolean> = _isActive.asStateFlow()

    fun start() {
        if (visualizer != null) return

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) 
            != PackageManager.PERMISSION_GRANTED) return
        
        runCatching {
            visualizer = Visualizer(0).apply {
                captureSize = Visualizer.getCaptureSizeRange()[1]
                setDataCaptureListener(object : Visualizer.OnDataCaptureListener {
                    override fun onWaveFormDataCapture(v: Visualizer?, waveform: ByteArray?, samplingRate: Int) {
                        if (waveform == null) return
                        var sum = 0.0
                        for (i in waveform.indices) {
                            val sample = (waveform[i].toInt() and 0xFF) - 128
                            sum += sample * sample
                        }
                        val rms = sqrt(sum / waveform.size)
                        val rawEnergy = (rms / 32.0).toFloat().coerceIn(0f, 1f)
                        _energy.value = _energy.value + 0.4f * (rawEnergy - _energy.value)
                        _isActive.value = _energy.value > 0.01f
                    }

                    override fun onFftDataCapture(v: Visualizer?, fft: ByteArray?, samplingRate: Int) {
                        if (fft == null) return
                        
                        // Extract Bass (low freq), Mid (vocals/instruments)
                        var bassSum = 0f
                        var midSum = 0f
                        
                        // FFT data format: [real0, imag0, real1, imag1, ...]
                        // Low bins are bass, mid bins are vocal range
                        for (i in 2..10 step 2) { // Bass 
                            val real = fft[i].toFloat()
                            val imag = fft[i+1].toFloat()
                            bassSum += sqrt(real * real + imag * imag)
                        }
                        for (i in 12..40 step 2) { // Mids
                            val real = fft[i].toFloat()
                            val imag = fft[i+1].toFloat()
                            midSum += sqrt(real * real + imag * imag)
                        }

                        val rawBass = (bassSum / 120f).coerceIn(0f, 1f)
                        val rawMid = (midSum / 250f).coerceIn(0f, 1f)

                        _bass.value = _bass.value + 0.5f * (rawBass - _bass.value)
                        _mid.value = _mid.value + 0.3f * (rawMid - _mid.value)
                    }
                }, Visualizer.getMaxCaptureRate() / 2, true, true)
                
                enabled = true
            }
        }
    }

    fun stop() {
        runCatching {
            visualizer?.enabled = false
            visualizer?.release()
        }
        visualizer = null
        _energy.value = 0f
        _bass.value = 0f
        _mid.value = 0f
        _isActive.value = false
    }
}
