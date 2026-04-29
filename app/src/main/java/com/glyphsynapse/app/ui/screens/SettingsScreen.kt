package com.glyphsynapse.app.ui.screens

import android.content.ComponentName
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.glyphsynapse.app.ui.components.DotMatrixBackground
import com.glyphsynapse.app.ui.components.NothingDivider
import com.glyphsynapse.app.ui.components.NothingSlider
import com.glyphsynapse.app.ui.components.NothingSwitch
import com.glyphsynapse.app.ui.components.SectionHeader
import com.glyphsynapse.app.ui.components.SettingsRow
import com.glyphsynapse.app.ui.theme.AccentWhite
import com.glyphsynapse.app.ui.theme.Background
import com.glyphsynapse.app.ui.theme.GlyphTypography
import com.glyphsynapse.app.ui.theme.TextSecondary
import com.glyphsynapse.app.ui.viewmodel.SettingsViewModel

@Composable
fun SettingsScreen(
    onNavigateToAdvanced: () -> Unit = {},
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    Box(modifier = Modifier.fillMaxSize().background(Background)) {
        DotMatrixBackground()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Column(modifier = Modifier.padding(horizontal = 24.dp, vertical = 24.dp)) {
                Text("SETTINGS", style = GlyphTypography.headlineLarge)
            }

            // ── Playback ─────────────────────────────────────────────────────────
            SectionHeader("PLAYBACK")
            NothingDivider()

            NothingSlider(
                value = state.speedMultiplier,
                onValueChange = viewModel::setSpeed,
                valueRange = 0.25f..4.0f,
                label = "SPEED",
                valueLabel = "${String.format("%.2f", state.speedMultiplier)}x",
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 12.dp)
            )

            NothingSlider(
                value = state.brightness,
                onValueChange = viewModel::setBrightness,
                valueRange = 0f..1f,
                label = "BRIGHTNESS",
                valueLabel = "${(state.brightness * 100).toInt()}%",
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 12.dp)
            )

            Spacer(Modifier.height(8.dp))
            NothingDivider()

            // ── Modes ─────────────────────────────────────────────────────────────
            SectionHeader("MODES")
            NothingDivider()

            SettingsRow(
                label = "STEALTH MODE",
                sublabel = "Caps brightness to 15% for sleep",
                control = {
                    NothingSwitch(
                        checked = state.stealthMode,
                        onCheckedChange = viewModel::setStealthMode
                    )
                }
            )
            NothingDivider()

            SettingsRow(
                label = "CHARGING FILL",
                sublabel = "Prioritize battery status when plugged in",
                control = {
                    NothingSwitch(
                        checked = state.chargingLock,
                        onCheckedChange = viewModel::setChargingLock
                    )
                }
            )
            NothingDivider()

            // ── System ────────────────────────────────────────────────────────────
            SectionHeader("SYSTEM INTEGRATION")
            NothingDivider()

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        val actions = listOf(
                            "com.nothing.settings.GLYPH_INTERFACE",
                            "android.settings.ACTION_GLYPH_INTERFACE", // Future proofing
                            "com.nothing.settings.glyph.GlyphInterfaceActivity"
                        )
                        
                        var started = false
                        for (action in actions) {
                            try {
                                val intent = Intent(action).apply {
                                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                }
                                context.startActivity(intent)
                                started = true
                                break
                            } catch (e: Exception) {
                                // Try next
                            }
                        }
                        
                        if (!started) {
                            try {
                                val intent = Intent().apply {
                                    component = ComponentName("com.nothing.settings", "com.nothing.settings.glyph.GlyphInterfaceActivity")
                                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                }
                                context.startActivity(intent)
                                started = true
                            } catch (e: Exception) {
                                // Final fallback
                                context.startActivity(Intent(android.provider.Settings.ACTION_SETTINGS))
                            }
                        }
                    }
                    .padding(horizontal = 24.dp, vertical = 20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("GLYPH TOYS", style = GlyphTypography.bodyLarge)
                    Text("Enable Synapse AOD in System Settings", style = GlyphTypography.bodyMedium, color = TextSecondary)
                }
                Text(">", style = GlyphTypography.headlineMedium, color = AccentWhite)
            }
            NothingDivider()

            // ── Advanced ──────────────────────────────────────────────────────────
            if (onNavigateToAdvanced != {}) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(onClick = onNavigateToAdvanced)
                        .padding(horizontal = 24.dp, vertical = 18.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("ADVANCED TUNING", style = GlyphTypography.labelSmall, color = TextSecondary, modifier = Modifier.weight(1f))
                    Text(">", style = GlyphTypography.bodyMedium, color = AccentWhite)
                }
                NothingDivider()
            }

            // ── Debug ─────────────────────────────────────────────────────────────
            SectionHeader("DIAGNOSTICS")
            NothingDivider()
            DebugRow("MODEL",           state.debug.buildModel)
            DebugRow("AUDIO ENERGY",    String.format("%.2f", state.debug.audioEnergy))
            DebugRow("SDK CONNECTED",   state.debug.sdkConnected.toString().uppercase())
            NothingDivider()

            Spacer(Modifier.height(48.dp))
        }
    }
}

@Composable
private fun DebugRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, style = GlyphTypography.labelSmall, color = TextSecondary, modifier = Modifier.weight(1f))
        Text(value.uppercase(), style = GlyphTypography.bodyMedium, color = AccentWhite)
    }
}
