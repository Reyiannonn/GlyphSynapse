package com.glyphsynapse.app.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.glyphsynapse.app.data.datastore.UserPreferencesRepository
import com.glyphsynapse.app.data.glyph.GlyphMatrixDevice
import com.glyphsynapse.app.data.glyph.GlyphManagerWrapper
import com.glyphsynapse.app.domain.animation.AnimationDefinition
import com.glyphsynapse.app.domain.animation.AnimationRegistry
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AdvancedUiState(
    val notificationAware: Boolean = false,
    val scheduleEnabled: Boolean = false,
    val device: GlyphMatrixDevice = GlyphMatrixDevice.Stub,
    val availableAnimations: List<AnimationDefinition> = emptyList()
)

@HiltViewModel
class AdvancedViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val prefs: UserPreferencesRepository,
    private val glyphManager: GlyphManagerWrapper
) : ViewModel() {

    val uiState: StateFlow<AdvancedUiState> = combine(
        prefs.notificationAware,
        prefs.scheduleEnabled
    ) { notifAware, schedEnabled ->
        AdvancedUiState(
            notificationAware = notifAware,
            scheduleEnabled = schedEnabled,
            device = glyphManager.device,
            availableAnimations = AnimationRegistry.all(context)
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), AdvancedUiState())

    fun setNotificationAware(enabled: Boolean) { viewModelScope.launch { prefs.setNotificationAware(enabled) } }
    fun setScheduleEnabled(enabled: Boolean) { viewModelScope.launch { prefs.setScheduleEnabled(enabled) } }
}
