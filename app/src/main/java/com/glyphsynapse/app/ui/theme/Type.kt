package com.glyphsynapse.app.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.glyphsynapse.app.R

val SpaceMono = FontFamily(
    Font(R.font.spacemono_regular, FontWeight.Normal),
    Font(R.font.spacemono_bold, FontWeight.Bold)
)

/**
 * Ndot55 is Nothing's proprietary font.
 * To enable it: place ndot55.ttf in res/font/ then change this to:
 *   FontFamily(Font(R.font.ndot55, FontWeight.Normal))
 * Until then, headings use Space Mono and the DotMatrixText Canvas renderer.
 */
val NdotFamily = SpaceMono

val GlyphTypography = Typography(
    displayLarge = TextStyle(
        fontFamily = NdotFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 40.sp,
        color = TextPrimary
    ),
    headlineLarge = TextStyle(
        fontFamily = NdotFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 28.sp,
        color = TextPrimary
    ),
    headlineMedium = TextStyle(
        fontFamily = NdotFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        color = TextPrimary
    ),
    titleLarge = TextStyle(
        fontFamily = NdotFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 18.sp,
        color = TextPrimary
    ),
    bodyLarge = TextStyle(
        fontFamily = SpaceMono,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        color = TextPrimary
    ),
    bodyMedium = TextStyle(
        fontFamily = SpaceMono,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        color = TextSecondary
    ),
    labelSmall = TextStyle(
        fontFamily = SpaceMono,
        fontWeight = FontWeight.Normal,
        fontSize = 10.sp,
        color = TextSecondary
    )
)
