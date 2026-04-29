package com.glyphsynapse.app.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import com.glyphsynapse.app.ui.theme.AccentRed
import com.glyphsynapse.app.ui.theme.AccentWhite
import com.glyphsynapse.app.ui.theme.Background
import com.glyphsynapse.app.ui.theme.Divider
import com.glyphsynapse.app.ui.theme.GlyphTypography
import com.glyphsynapse.app.ui.theme.Surface
import com.glyphsynapse.app.ui.theme.SurfaceElevated
import com.glyphsynapse.app.ui.theme.TextPrimary
import com.glyphsynapse.app.ui.theme.TextSecondary

/** Nothing-style rectangular toggle switch with haptics. */
@Composable
fun NothingSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val haptic = LocalHapticFeedback.current
    val trackColor = if (checked) AccentRed else Color.Transparent
    val borderColor = if (checked) AccentRed else Divider

    Box(
        modifier = modifier
            .size(width = 44.dp, height = 24.dp)
            .border(1.dp, borderColor, RectangleShape)
            .background(trackColor)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { 
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                onCheckedChange(!checked) 
            },
        contentAlignment = if (checked) Alignment.CenterEnd else Alignment.CenterStart
    ) {
        Box(
            modifier = Modifier
                .padding(horizontal = 3.dp)
                .size(width = 14.dp, height = 18.dp)
                .background(if (checked) AccentWhite else TextSecondary)
        )
    }
}

/** Nothing-style slider with haptic ticks. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NothingSlider(
    value: Float,
    onValueChange: (Float) -> Unit,
    modifier: Modifier = Modifier,
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
    label: String = "",
    valueLabel: String = ""
) {
    val haptic = LocalHapticFeedback.current
    
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (label.isNotEmpty()) {
            Text(
                text = label,
                style = GlyphTypography.bodyLarge,
                color = TextPrimary,
                modifier = Modifier.width(110.dp)
            )
        }
        Slider(
            value = value,
            onValueChange = {
                if ((it * 100).toInt() != (value * 100).toInt()) {
                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                }
                onValueChange(it)
            },
            valueRange = valueRange,
            modifier = Modifier.weight(1f),
            colors = SliderDefaults.colors(
                thumbColor = AccentWhite,
                activeTrackColor = AccentWhite,
                inactiveTrackColor = Divider
            )
        )
        if (valueLabel.isNotEmpty()) {
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = valueLabel,
                style = GlyphTypography.bodyMedium,
                color = TextSecondary,
                modifier = Modifier.width(48.dp)
            )
        }
    }
}

/** Subtle dot-matrix background pattern for that OEM feel. */
@Composable
fun DotMatrixBackground(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier.fillMaxSize()) {
        val dotSize = 1.dp.toPx()
        val spacing = 16.dp.toPx()
        
        for (x in 0..size.width.toInt() step spacing.toInt()) {
            for (y in 0..size.height.toInt() step spacing.toInt()) {
                drawCircle(
                    color = Color.White.copy(alpha = 0.05f),
                    radius = dotSize / 2,
                    center = Offset(x.toFloat(), y.toFloat())
                )
            }
        }
    }
}

/** A single settings row with a label on the left and a control on the right. */
@Composable
fun SettingsRow(
    label: String,
    modifier: Modifier = Modifier,
    sublabel: String = "",
    control: @Composable () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        androidx.compose.foundation.layout.Column(modifier = Modifier.weight(1f)) {
            Text(label, style = GlyphTypography.bodyLarge, color = TextPrimary)
            if (sublabel.isNotEmpty()) {
                Text(sublabel, style = GlyphTypography.bodyMedium, color = TextSecondary)
            }
        }
        control()
    }
}

/** Horizontal hairline divider in Nothing style. */
@Composable
fun NothingDivider(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(Divider)
    )
}

/** Section header text. */
@Composable
fun SectionHeader(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text,
        style = GlyphTypography.labelSmall,
        color = TextSecondary,
        modifier = modifier.padding(horizontal = 24.dp, vertical = 8.dp)
    )
}
