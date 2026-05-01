package com.glyphsynapse.app.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.glyphsynapse.app.ui.components.DotMatrixBackground
import com.glyphsynapse.app.ui.components.GlyphPreview
import com.glyphsynapse.app.ui.components.NothingDivider
import com.glyphsynapse.app.ui.components.NothingSwitch
import com.glyphsynapse.app.ui.components.SettingsRow
import com.glyphsynapse.app.ui.theme.AccentRed
import com.glyphsynapse.app.ui.theme.Background
import com.glyphsynapse.app.ui.theme.GlyphTypography
import com.glyphsynapse.app.ui.theme.TextSecondary
import com.glyphsynapse.app.ui.viewmodel.HomeViewModel

@Composable
fun HomeScreen(viewModel: HomeViewModel = hiltViewModel()) {
    val context = LocalContext.current
    // Only recomposes when main settings change (rarely)
    val state by viewModel.uiState.collectAsState()
    // Recomposes only the preview (30fps)
    val pixelFrame by viewModel.pixelFrame.collectAsState()

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            viewModel.toggleService(true)
        }
    }

    if (!state.isCompatibleDevice) {
        IncompatibleDeviceScreen()
        return
    }

    Box(modifier = Modifier.fillMaxSize().background(Background)) {
        DotMatrixBackground()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(Modifier.height(32.dp))

            Text(
                text = "GLYPH SYNAPSE",
                style = GlyphTypography.displayLarge,
                modifier = Modifier.padding(horizontal = 24.dp)
            )

            Spacer(Modifier.height(4.dp))

            Text(
                text = "Always-On Display controller",
                style = GlyphTypography.bodyMedium,
                color = TextSecondary,
                modifier = Modifier.padding(horizontal = 24.dp)
            )

            Spacer(Modifier.height(48.dp))

            // Large Glyph preview
            state.selectedAnimation?.let { anim ->
                GlyphPreview(
                    animation = anim,
                    device = state.device,
                    speedMultiplier = state.speedMultiplier,
                    brightness = if (state.stealthMode) state.brightness * 0.15f else state.brightness,
                    externalPixels = pixelFrame?.pixels,
                    modifier = Modifier
                        .width(220.dp)
                        .align(Alignment.CenterHorizontally)
                )
            }

            Spacer(Modifier.height(48.dp))
            NothingDivider()

            // Active animation label
            state.selectedAnimation?.let { anim ->
                Column(Modifier.padding(horizontal = 24.dp, vertical = 24.dp)) {
                    Text(
                        text = anim.name.uppercase(),
                        style = GlyphTypography.headlineMedium
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = anim.description,
                        style = GlyphTypography.bodyMedium,
                        color = TextSecondary
                    )
                }
            }

            NothingDivider()

            SettingsRow(
                label = "GLYPH SYNAPSE",
                sublabel = if (state.serviceEnabled) "ACTIVE — DRIVING AOD" else "TAP TO ACTIVATE",
                control = {
                    NothingSwitch(
                        checked = state.serviceEnabled,
                        onCheckedChange = { enabled ->
                            if (enabled) {
                                val hasPermission = ContextCompat.checkSelfPermission(
                                    context, Manifest.permission.RECORD_AUDIO
                                ) == PackageManager.PERMISSION_GRANTED
                                
                                if (hasPermission) {
                                    viewModel.toggleService(true)
                                } else {
                                    launcher.launch(Manifest.permission.RECORD_AUDIO)
                                }
                            } else {
                                viewModel.toggleService(false)
                            }
                        }
                    )
                }
            )

            NothingDivider()
            Spacer(Modifier.height(48.dp))
        }
    }
}

@Composable
private fun IncompatibleDeviceScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Background),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(40.dp)
        ) {
            Text(
                text = "INCOMPATIBLE DEVICE",
                style = GlyphTypography.headlineLarge,
                color = AccentRed
            )
            Spacer(Modifier.height(16.dp))
            Text(
                text = "GlyphSynapse requires a Nothing Phone (3) or Nothing Phone (4a) Pro with Glyph Matrix support.",
                style = GlyphTypography.bodyLarge,
                color = TextSecondary
            )
        }
    }
}
