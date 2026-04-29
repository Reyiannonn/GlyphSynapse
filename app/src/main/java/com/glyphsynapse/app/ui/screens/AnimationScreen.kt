package com.glyphsynapse.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.glyphsynapse.app.ui.components.AnimationCard
import com.glyphsynapse.app.ui.components.DotMatrixBackground
import com.glyphsynapse.app.ui.theme.Background
import com.glyphsynapse.app.ui.theme.GlyphTypography
import com.glyphsynapse.app.ui.theme.TextSecondary
import com.glyphsynapse.app.ui.viewmodel.AnimationsViewModel

@Composable
fun AnimationScreen(viewModel: AnimationsViewModel = hiltViewModel()) {
    val state by viewModel.uiState.collectAsState()

    Box(modifier = Modifier.fillMaxSize().background(Background)) {
        DotMatrixBackground()

        Column(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier.padding(horizontal = 24.dp, vertical = 24.dp)) {
                Text("ANIMATIONS", style = GlyphTypography.headlineLarge)
                Spacer(Modifier.height(4.dp))
                Text(
                    "${state.animations.size} SCENES AVAILABLE",
                    style = GlyphTypography.bodyMedium,
                    color = TextSecondary
                )
            }

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(state.animations, key = { it.name }) { animation ->
                    AnimationCard(
                        animation = animation,
                        device = state.device,
                        isSelected = animation.name == state.selectedName,
                        speedMultiplier = state.speedMultiplier,
                        onClick = { viewModel.selectAnimation(animation.name) }
                    )
                }
            }
        }
    }
}
