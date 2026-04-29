package com.glyphsynapse.app.data.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.glyphsynapse.app.domain.model.ScheduleWindow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserPreferencesRepository @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    private object Keys {
        val SELECTED_ANIMATION  = stringPreferencesKey("selected_animation")
        val SPEED_MULTIPLIER    = floatPreferencesKey("speed_multiplier")
        val BRIGHTNESS          = floatPreferencesKey("brightness")
        val STEALTH_MODE        = booleanPreferencesKey("stealth_mode")
        val SERVICE_ENABLED     = booleanPreferencesKey("service_enabled")
        val CHARGING_LOCK       = booleanPreferencesKey("charging_lock")
        val NOTIFICATION_AWARE  = booleanPreferencesKey("notification_aware")
        val SCHEDULE_ENABLED    = booleanPreferencesKey("schedule_enabled")
        val SCHEDULE_JSON       = stringPreferencesKey("schedule_json")
        val PRESETS_JSON        = stringPreferencesKey("presets_json")
    }

    val selectedAnimation: Flow<String>   = dataStore.data.map { it[Keys.SELECTED_ANIMATION] ?: "Breathe" }
    val speedMultiplier: Flow<Float>      = dataStore.data.map { (it[Keys.SPEED_MULTIPLIER] ?: 1.0f).coerceIn(0.25f, 4.0f) }
    val brightness: Flow<Float>           = dataStore.data.map { (it[Keys.BRIGHTNESS] ?: 0.8f).coerceIn(0f, 1f) }
    val stealthMode: Flow<Boolean>        = dataStore.data.map { it[Keys.STEALTH_MODE] ?: false }
    val serviceEnabled: Flow<Boolean>     = dataStore.data.map { it[Keys.SERVICE_ENABLED] ?: false }
    val chargingLock: Flow<Boolean>       = dataStore.data.map { it[Keys.CHARGING_LOCK] ?: true }
    val notificationAware: Flow<Boolean>  = dataStore.data.map { it[Keys.NOTIFICATION_AWARE] ?: false }
    val scheduleEnabled: Flow<Boolean>    = dataStore.data.map { it[Keys.SCHEDULE_ENABLED] ?: false }

    val scheduleWindow: Flow<ScheduleWindow> = dataStore.data.map { prefs ->
        prefs[Keys.SCHEDULE_JSON]?.let {
            runCatching { Json.decodeFromString<ScheduleWindow>(it) }.getOrNull()
        } ?: ScheduleWindow(22, 0, 7, 0)
    }

    val presetsJson: Flow<String> = dataStore.data.map { it[Keys.PRESETS_JSON] ?: "[]" }

    suspend fun setSelectedAnimation(name: String)   = dataStore.edit { it[Keys.SELECTED_ANIMATION] = name }
    suspend fun setSpeedMultiplier(value: Float)     = dataStore.edit { it[Keys.SPEED_MULTIPLIER] = value.coerceIn(0.25f, 4.0f) }
    suspend fun setBrightness(value: Float)          = dataStore.edit { it[Keys.BRIGHTNESS] = value.coerceIn(0f, 1f) }
    suspend fun setStealthMode(enabled: Boolean)     = dataStore.edit { it[Keys.STEALTH_MODE] = enabled }
    suspend fun setServiceEnabled(enabled: Boolean)  = dataStore.edit { it[Keys.SERVICE_ENABLED] = enabled }
    suspend fun setChargingLock(enabled: Boolean)    = dataStore.edit { it[Keys.CHARGING_LOCK] = enabled }
    suspend fun setNotificationAware(enabled: Boolean) = dataStore.edit { it[Keys.NOTIFICATION_AWARE] = enabled }
    suspend fun setScheduleEnabled(enabled: Boolean) = dataStore.edit { it[Keys.SCHEDULE_ENABLED] = enabled }

    suspend fun setScheduleWindow(window: ScheduleWindow) = dataStore.edit {
        it[Keys.SCHEDULE_JSON] = Json.encodeToString(window)
    }

    suspend fun setPresetsJson(json: String) = dataStore.edit { it[Keys.PRESETS_JSON] = json }
}
