package com.glyphsynapse.app.domain.usecase

import com.glyphsynapse.app.data.datastore.UserPreferencesRepository
import com.glyphsynapse.app.domain.model.Preset
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.UUID
import javax.inject.Inject

class LoadPresetsUseCase @Inject constructor(
    private val prefs: UserPreferencesRepository
) {
    operator fun invoke(): Flow<List<Preset>> =
        prefs.presetsJson.map { json ->
            runCatching { Json.decodeFromString<List<Preset>>(json) }.getOrDefault(emptyList())
        }
}

class SavePresetUseCase @Inject constructor(
    private val prefs: UserPreferencesRepository,
    private val loadPresets: LoadPresetsUseCase
) {
    suspend operator fun invoke(preset: Preset) {
        val existing = loadPresets().first()
        val updated = existing.filter { it.id != preset.id } + preset
        prefs.setPresetsJson(Json.encodeToString(updated))
    }
}

class DeletePresetUseCase @Inject constructor(
    private val prefs: UserPreferencesRepository,
    private val loadPresets: LoadPresetsUseCase
) {
    suspend operator fun invoke(presetId: String) {
        val existing = loadPresets().first()
        val updated = existing.filter { it.id != presetId }
        prefs.setPresetsJson(Json.encodeToString(updated))
    }
}

class ExportPresetUseCase @Inject constructor() {
    operator fun invoke(preset: Preset): String = Json.encodeToString(preset)
}

class ImportPresetUseCase @Inject constructor(
    private val savePreset: SavePresetUseCase
) {
    suspend operator fun invoke(json: String): Result<Preset> = runCatching {
        val preset = Json.decodeFromString<Preset>(json)
        val withNewId = preset.copy(id = UUID.randomUUID().toString())
        savePreset(withNewId)
        withNewId
    }
}
