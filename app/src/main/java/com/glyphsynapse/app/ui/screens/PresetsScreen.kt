package com.glyphsynapse.app.ui.screens

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.glyphsynapse.app.domain.model.Preset
import com.glyphsynapse.app.ui.components.NothingDivider
import com.glyphsynapse.app.ui.theme.AccentRed
import com.glyphsynapse.app.ui.theme.AccentWhite
import com.glyphsynapse.app.ui.theme.Background
import com.glyphsynapse.app.ui.theme.Divider
import com.glyphsynapse.app.ui.theme.GlyphTypography
import com.glyphsynapse.app.ui.theme.Surface
import com.glyphsynapse.app.ui.theme.TextPrimary
import com.glyphsynapse.app.ui.theme.TextSecondary
import com.glyphsynapse.app.ui.viewmodel.PresetsViewModel

@Composable
fun PresetsScreen(viewModel: PresetsViewModel = hiltViewModel()) {
    val presets by viewModel.presets.collectAsState()
    var newPresetName by remember { mutableStateOf("") }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
    ) {
        Column(modifier = Modifier.padding(horizontal = 24.dp, vertical = 24.dp)) {
            Text("Presets", style = GlyphTypography.headlineLarge)
            Spacer(Modifier.height(4.dp))
            Text("${presets.size} saved", style = GlyphTypography.bodyMedium, color = TextSecondary)
        }

        NothingDivider()

        // Save current as new preset
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BasicTextField(
                value = newPresetName,
                onValueChange = { newPresetName = it },
                textStyle = GlyphTypography.bodyLarge.copy(color = TextPrimary),
                cursorBrush = SolidColor(AccentWhite),
                decorationBox = { inner ->
                    Row(
                        modifier = Modifier
                            .weight(1f)
                            .border(1.dp, Divider, RectangleShape)
                            .padding(horizontal = 12.dp, vertical = 10.dp)
                    ) {
                        if (newPresetName.isEmpty()) {
                            Text("Preset name", style = GlyphTypography.bodyLarge, color = TextSecondary)
                        }
                        inner()
                    }
                },
                modifier = Modifier.weight(1f)
            )
            Text(
                text = "SAVE",
                style = GlyphTypography.titleLarge,
                color = if (newPresetName.isNotBlank()) AccentWhite else TextSecondary,
                modifier = Modifier.clickable(enabled = newPresetName.isNotBlank()) {
                    viewModel.saveCurrentAsPreset(newPresetName.trim())
                    newPresetName = ""
                }
            )
        }

        NothingDivider()

        LazyColumn {
            items(presets, key = { it.id }) { preset ->
                PresetRow(
                    preset = preset,
                    onApply = { viewModel.applyPreset(preset) },
                    onDelete = { viewModel.deletePreset(preset.id) },
                    onExport = {
                        val json = viewModel.exportPreset(preset)
                        val intent = Intent(Intent.ACTION_SEND).apply {
                            type = "text/plain"
                            putExtra(Intent.EXTRA_TEXT, json)
                            putExtra(Intent.EXTRA_SUBJECT, "GlyphSynapse Preset: ${preset.name}")
                        }
                        context.startActivity(Intent.createChooser(intent, "Share Preset"))
                    }
                )
                NothingDivider()
            }
        }
    }
}

@Composable
private fun PresetRow(
    preset: Preset,
    onApply: () -> Unit,
    onDelete: () -> Unit,
    onExport: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Surface)
            .padding(horizontal = 24.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(preset.name, style = GlyphTypography.titleLarge)
            Text(
                "${preset.animationName}  ·  ${String.format("%.1f", preset.speedMultiplier)}×  ·  ${(preset.brightness * 100).toInt()}%",
                style = GlyphTypography.bodyMedium,
                color = TextSecondary
            )
        }
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Text("APPLY", style = GlyphTypography.bodyMedium, color = AccentWhite,
                modifier = Modifier.clickable(onClick = onApply))
            Text("SHARE", style = GlyphTypography.bodyMedium, color = TextSecondary,
                modifier = Modifier.clickable(onClick = onExport))
            Text("DEL", style = GlyphTypography.bodyMedium, color = AccentRed,
                modifier = Modifier.clickable(onClick = onDelete))
        }
    }
}
