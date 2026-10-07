package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val GroupByColorScheme = darkColorScheme(
    primary = PrimaryOrange,
    onPrimary = TextPrimary,
    primaryContainer = PrimaryOrangeDim,
    onPrimaryContainer = PrimaryOrange,
    secondary = AccentCyan,
    onSecondary = BgDark,
    secondaryContainer = AccentCyanDim,
    onSecondaryContainer = AccentCyan,
    tertiary = AccentPurple,
    error = AlertRed,
    errorContainer = AlertRedDim,
    onError = TextPrimary,
    background = BgDark,
    onBackground = TextPrimary,
    surface = SurfaceDark,
    onSurface = TextPrimary,
    surfaceVariant = SurfaceCard,
    onSurfaceVariant = TextSecondary,
    outline = BorderSubtle,
    outlineVariant = BorderHighlight
)

@Composable
fun MyApplicationTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = GroupByColorScheme,
        typography = Typography,
        content = content
    )
}

