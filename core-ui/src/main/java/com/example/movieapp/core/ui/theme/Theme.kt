package com.example.movieapp.core.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val ColorLightSurfaceVariant = Color(0xFFE8E8ED)
private val ColorLightTextVariant = Color(0xFF565660)

private val MovieDarkColors = darkColorScheme(
    primary = MovieRed,
    onPrimary = MovieWhite,
    secondary = MovieGray,
    background = MovieBlack,
    onBackground = MovieWhite,
    surface = MovieSurface,
    onSurface = MovieWhite,
    surfaceVariant = MovieSurfaceVariant,
    onSurfaceVariant = MovieGray,
)

private val MovieLightColors = lightColorScheme(
    primary = MovieRed,
    onPrimary = MovieWhite,
    secondary = MovieRedDark,
    background = MovieLightBackground,
    onBackground = MovieLightText,
    surface = MovieLightSurface,
    onSurface = MovieLightText,
    surfaceVariant = ColorLightSurfaceVariant,
    onSurfaceVariant = ColorLightTextVariant,
)

/** Applies the MovieApp Material 3 theme, dark by default on supported devices. */
@Composable
fun MovieAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = if (darkTheme) MovieDarkColors else MovieLightColors,
        typography = MovieTypography,
        content = content,
    )
}
