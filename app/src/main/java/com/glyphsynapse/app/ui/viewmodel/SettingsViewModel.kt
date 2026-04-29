package com.glyphsynapse.app.ui.viewmodel

import android.content.Context
import android.os.Build
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.glyphsynapse.app.data.datastore.UserPreferencesRepository
import com.glyphsynapse.app.data.glyph.GlyphManagerWrapper
import com.glyphsynapse.app.domain.engine.AudioAwareness
import com.glyphsynapse.app.domain.model.ScheduleWindow
import com.glyphsynapse.app.service.GlyphAnimationService
import com.nothing.ketchum.Common
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class DebugInfo(
    val buildModel: String = Build.MODEL,
    val buildDevice: String = Build.DEVICE,
    val detectedDevice: String = "Unknown",
    val serviceEnabled: Boolean = false,
    val initCalled: Boolean = false,
    val callbackFired: Boolean = false,
    val registerResult: String = "not called",
    val sdkConnected: Boolean = false,
    val is23112: Boolean = false,
    val is25111p: Boolean = false,
    val matrixLength: Int = 0,
    val audioEnergy: Float = 0f,
    val audioActive: Boolean = false
)

data class SettingsUiState(
    val speedMultiplier: Float = 1f,
    val brightness: Float = 0.8f,
    val stealthMode: Boolean = false,
    val chargingLock: Boolean = true,
    val notificationAware: Boolean = false,
    val scheduleEnabled: Boolean = false,
    val scheduleWindow: ScheduleWindow = ScheduleWindow(22, 0, 7, 0),
    val deviceModel: String = Build.MODEL,
    val debug: DebugInfo = DebugInfo()
)

private data class SettingsPart1(
    val speed: Float, val brightness: Float, val stealth: Boolean,
    val chargingLock: Boolean, val notifAware: Boolean
)

private data class SdkDiag(
    val serviceEnabled: Boolean,
    val initCalled: Boolean,
    val callbackFired: Boolean,
    val registerResult: String,
    val connected: Boolean,
    val audioEnergy: Float,
    val audioActive: Boolean
)

@OptIn(FlowPreview::class)
@HiltViewModel
class SettingsViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val prefs: UserPreferencesRepository,
    private val glyphManager: GlyphManagerWrapper,
    private val audioAwareness: AudioAwareness
) : ViewModel() {

    private val _speed = MutableStateFlow(1f)
    private val _brightness = MutableStateFlow(0.8f)

    init {
        viewModelScope.launch {
            _speed.debounce(250).collect {
                prefs.setSpeedMultiplier(it)
                GlyphAnimationService.notifyUpdate(context)
            }
        }
        viewModelScope.launch {
            _brightness.debounce(250).collect {
                prefs.setBrightness(it)
                GlyphAnimationService.notifyUpdate(context)
            }
        }
    }

    private val part1 = combine(
        prefs.speedMultiplier,
        prefs.brightness,
        prefs.stealthMode,
        prefs.chargingLock,
        prefs.notificationAware
    ) { speed, brightness, stealth, chargingLock, notifAware ->
        SettingsPart1(speed, brightness, stealth, chargingLock, notifAware)
    }

    private val sdkDiag = combine(
        prefs.serviceEnabled,
        glyphManager.initCalled,
        glyphManager.callbackFired,
        glyphManager.registerResult,
        glyphManager.isConnected,
        audioAwareness.energy,
        audioAwareness.isActive
    ) { args ->
        SdkDiag(
            serviceEnabled = args[0] as Boolean,
            initCalled = args[1] as Boolean,
            callbackFired = args[2] as Boolean,
            registerResult = args[3] as String,
            connected = args[4] as Boolean,
            audioEnergy = args[5] as Float,
            audioActive = args[6] as Boolean
        )
    }

    val uiState: StateFlow<SettingsUiState> = combine(
        part1,
        prefs.scheduleEnabled,
        prefs.scheduleWindow,
        sdkDiag
    ) { p1, schedEnabled, schedWindow, diag ->
        SettingsUiState(
            speedMultiplier = p1.speed,
            brightness = p1.brightness,
            stealthMode = p1.stealth,
            chargingLock = p1.chargingLock,
            notificationAware = p1.notifAware,
            scheduleEnabled = schedEnabled,
            scheduleWindow = schedWindow,
            debug = DebugInfo(
                buildModel     = Build.MODEL,
                buildDevice    = Build.DEVICE,
                detectedDevice = glyphManager.device.displayName,
                serviceEnabled = diag.serviceEnabled,
                initCalled     = diag.initCalled,
                callbackFired  = diag.callbackFired,
                registerResult = diag.registerResult,
                sdkConnected   = diag.connected,
                is23112        = runCatching { Common.is23112()  }.getOrDefault(false),
                is25111p       = runCatching { Common.is25111p() }.getOrDefault(false),
                matrixLength   = runCatching { Common.getDeviceMatrixLength() }.getOrDefault(-1),
                audioEnergy    = diag.audioEnergy,
                audioActive    = diag.audioActive
            )
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), SettingsUiState())

    fun setSpeed(value: Float) { _speed.value = value.coerceIn(0.25f, 4.0f) }
    fun setBrightness(value: Float) { _brightness.value = value.coerceIn(0f, 1f) }
    fun setStealthMode(enabled: Boolean) {
        viewModelScope.launch { prefs.setStealthMode(enabled); GlyphAnimationService.notifyUpdate(context) }
    }
    fun setChargingLock(enabled: Boolean) {
        viewModelScope.launch { prefs.setChargingLock(enabled); GlyphAnimationService.notifyUpdate(context) }
    }
    fun setNotificationAware(enabled: Boolean) { viewModelScope.launch { prefs.setNotificationAware(enabled) } }
    fun setScheduleEnabled(enabled: Boolean) { viewModelScope.launch { prefs.setScheduleEnabled(enabled) } }
    fun setScheduleWindow(window: ScheduleWindow) { viewModelScope.launch { prefs.setScheduleWindow(window) } }
}
