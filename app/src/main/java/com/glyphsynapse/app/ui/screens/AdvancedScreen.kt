package com.glyphsynapse.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.glyphsynapse.app.ui.components.NothingDivider
import com.glyphsynapse.app.ui.components.NothingSwitch
import com.glyphsynapse.app.ui.components.SectionHeader
import com.glyphsynapse.app.ui.components.SettingsRow
import com.glyphsynapse.app.ui.theme.Background
import com.glyphsynapse.app.ui.theme.GlyphTypography
import com.glyphsynapse.app.ui.theme.TextSecondary
import com.glyphsynapse.app.ui.viewmodel.AdvancedViewModel

@Composable
fun AdvancedScreen(viewModel: AdvancedViewModel = hiltViewModel()) {
    val state by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .verticalScroll(rememberScrollState())
    ) {
        Column(modifier = Modifier.padding(horizontal = 24.dp, vertical = 24.dp)) {
            Text("Advanced", style = GlyphTypography.headlineLarge)
            Spacer(Modifier.height(4.dp))
            Text("Notification reactions and schedule", style = GlyphTypography.bodyMedium, color = TextSecondary)
        }

        NothingDivider()

        // ── Device Matrix Info ────────────────────────────────────────────────
        SectionHeader("GLYPH MATRIX")
        NothingDivider()

        SettingsRow(
            label = "Device",
            sublabel = state.device.displayName,
            control = {}
        )
        NothingDivider()

        SettingsRow(
            label = "Matrix Resolution",
            sublabel = "${state.device.matrixWidth} × ${state.device.matrixHeight} px",
            control = {}
        )
        NothingDivider()

        SettingsRow(
            label = "Total LEDs",
            sublabel = "${state.device.matrixSize}",
            control = {}
        )
        NothingDivider()

        // ── Notification Reactions ─────────────────────────────────────────────
        SectionHeader("NOTIFICATION REACTIONS")
        NothingDivider()

        SettingsRow(
            label = "Notification Aware",
            sublabel = "Flash matrix on calls, messages and alarms",
            control = {
                NothingSwitch(
                    checked = state.notificationAware,
                    onCheckedChange = viewModel::setNotificationAware
                )
            }
        )
        NothingDivider()

        Spacer(Modifier.height(32.dp))
    }
}
