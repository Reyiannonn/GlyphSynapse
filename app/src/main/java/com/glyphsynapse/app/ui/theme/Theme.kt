package com.glyphsynapse.app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val NothingColorScheme = darkColorScheme(
    background = Background,
    surface = Surface,
    surfaceVariant = SurfaceElevated,
    primary = AccentWhite,
    secondary = AccentRed,
    onBackground = TextPrimary,
    onSurface = TextPrimary,
    onSurfaceVariant = TextSecondary,
    outline = Divider
)

@Composable
fun GlyphSynapseTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = NothingColorScheme,
        typography = GlyphTypography,
        content = content
    )
}
