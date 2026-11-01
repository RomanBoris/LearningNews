package com.pobezhkin.learningnews.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val NewsColorScheme = darkColorScheme(
    primary = Lavender,
    onPrimary = Color(0xFF2F1D63),
    primaryContainer = Indigo,
    onPrimaryContainer = Color(0xFFEADDFF),

    secondary = Orchid,
    onSecondary = Color(0xFF4B1D3C),
    secondaryContainer = Plum,
    onSecondaryContainer = Color(0xFFFFD9EE),

    tertiary = Mauve,
    onTertiary = Color(0xFF40204F),
    tertiaryContainer = Violet,
    onTertiaryContainer = Color(0xFFF8DBFF),

    background = Ink,
    onBackground = TextPrimary,
    surface = Ink,
    onSurface = TextPrimary,
    surfaceVariant = SurfaceVariantDark,
    onSurfaceVariant = TextSecondary,
    surfaceTint = Lavender,

    inverseSurface = TextPrimary,
    inverseOnSurface = Color(0xFF322D3B),
    inversePrimary = Indigo,

    outline = Outline,
    outlineVariant = OutlineSoft,

    surfaceContainerLowest = SurfaceLowest,
    surfaceContainerLow = SurfaceLow,
    surfaceContainer = SurfaceMid,
    surfaceContainerHigh = SurfaceHigh,
    surfaceContainerHighest = SurfaceHighest
)

/**
 * Приложение всегда тёмное: цвета и градиенты подобраны под тёмные поверхности,
 * поэтому системные светлая тема и динамические цвета обоев не используются.
 */
@Composable
fun LearningNewsTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = NewsColorScheme,
        typography = Typography,
        content = content
    )
}
