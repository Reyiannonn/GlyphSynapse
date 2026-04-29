package com.glyphsynapse.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.glyphsynapse.app.data.datastore.UserPreferencesRepository
import com.glyphsynapse.app.domain.model.Preset
import com.glyphsynapse.app.domain.usecase.DeletePresetUseCase
import com.glyphsynapse.app.domain.usecase.ExportPresetUseCase
import com.glyphsynapse.app.domain.usecase.ImportPresetUseCase
import com.glyphsynapse.app.domain.usecase.LoadPresetsUseCase
import com.glyphsynapse.app.domain.usecase.SavePresetUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class PresetsViewModel @Inject constructor(
    private val prefs: UserPreferencesRepository,
    private val loadPresets: LoadPresetsUseCase,
    private val savePreset: SavePresetUseCase,
    private val deletePreset: DeletePresetUseCase,
    private val exportPreset: ExportPresetUseCase,
    private val importPreset: ImportPresetUseCase
) : ViewModel() {

    val presets: StateFlow<List<Preset>> = loadPresets()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun saveCurrentAsPreset(name: String) {
        viewModelScope.launch {
            val animName   = prefs.selectedAnimation.first()
            val speed      = prefs.speedMultiplier.first()
            val brightness = prefs.brightness.first()
            val stealth    = prefs.stealthMode.first()
            savePreset(
                Preset(
                    id = UUID.randomUUID().toString(),
                    name = name,
                    animationName = animName,
                    speedMultiplier = speed,
                    brightness = brightness,
                    stealthMode = stealth
                )
            )
        }
    }

    fun applyPreset(preset: Preset) {
        viewModelScope.launch {
            prefs.setSelectedAnimation(preset.animationName)
            prefs.setSpeedMultiplier(preset.speedMultiplier)
            prefs.setBrightness(preset.brightness)
            prefs.setStealthMode(preset.stealthMode)
        }
    }

    fun deletePreset(id: String) { viewModelScope.launch { deletePreset.invoke(id) } }

    fun exportPreset(preset: Preset): String = exportPreset.invoke(preset)

    fun importPreset(json: String, onResult: (Result<Preset>) -> Unit) {
        viewModelScope.launch { onResult(importPreset.invoke(json)) }
    }
}
