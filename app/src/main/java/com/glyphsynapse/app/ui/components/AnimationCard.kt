package com.glyphsynapse.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import com.glyphsynapse.app.data.glyph.GlyphMatrixDevice
import com.glyphsynapse.app.domain.animation.AnimationDefinition
import com.glyphsynapse.app.ui.theme.AccentRed
import com.glyphsynapse.app.ui.theme.Divider
import com.glyphsynapse.app.ui.theme.GlyphTypography
import com.glyphsynapse.app.ui.theme.Surface
import com.glyphsynapse.app.ui.theme.SurfaceElevated
import com.glyphsynapse.app.ui.theme.TextPrimary
import com.glyphsynapse.app.ui.theme.TextSecondary

/**
 * Animation selection card with a live mini GlyphPreview thumbnail.
 *
 * Layout:
 *  ┌──────────────────────────────┐
 *  │  [GlyphMiniPreview 80×80]    │
 *  │  BREATHE             ●       │  ← active dot (Nothing red)
 *  │  All glyphs, slow pulse      │
 *  └──────────────────────────────┘
 */
@Composable
fun AnimationCard(
    animation: AnimationDefinition,
    device: GlyphMatrixDevice,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    speedMultiplier: Float = 1f
) {
    val borderColor = if (isSelected) AccentRed else Divider

    Column(
        modifier = modifier
            .background(Surface)
            .border(1.dp, borderColor, RectangleShape)
            .clickable(onClick = onClick)
            .padding(12.dp)
    ) {
        GlyphPreview(
            animation = animation,
            device = device,
            speedMultiplier = speedMultiplier,
            brightness = 0.9f,
            modifier = Modifier
                .size(80.dp)
                .align(Alignment.CenterHorizontally),
            mini = true
        )

        Spacer(Modifier.height(10.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = animation.name.uppercase(),
                style = GlyphTypography.titleLarge,
                color = TextPrimary,
                modifier = Modifier.weight(1f)
            )
            if (isSelected) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .background(AccentRed)
                )
            }
        }

        Spacer(Modifier.height(4.dp))

        Text(
            text = animation.description,
            style = GlyphTypography.bodyMedium,
            color = TextSecondary
        )
    }
}
