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
                        // Smoother lerp for overall energy
                        _energy.value = _energy.value + 0.15f * (rawEnergy - _energy.value)
                        _isActive.value = _energy.value > 0.005f
                    }

                    override fun onFftDataCapture(v: Visualizer?, fft: ByteArray?, samplingRate: Int) {
                        if (fft == null) return
                        
                        var bassSum = 0f
                        var midSum = 0f
                        
                        // Expanded FFT bins for better sensitivity
                        // Low bins (0-150Hz approx)
                        for (i in 2..14 step 2) { 
                            val real = fft[i].toFloat()
                            val imag = fft[i+1].toFloat()
                            bassSum += sqrt(real * real + imag * imag)
                        }
                        // Mid bins (250Hz - 2kHz approx)
                        for (i in 16..120 step 2) { 
                            val real = fft[i].toFloat()
                            val imag = fft[i+1].toFloat()
                            midSum += sqrt(real * real + imag * imag)
                        }

                        val rawBass = (bassSum / 180f).coerceIn(0f, 1f)
                        val rawMid = (midSum / 400f).coerceIn(0f, 1f)

                        // Slower, more organic smoothing
                        _bass.value = _bass.value + 0.25f * (rawBass - _bass.value)
                        _mid.value = _mid.value + 0.15f * (rawMid - _mid.value)
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
