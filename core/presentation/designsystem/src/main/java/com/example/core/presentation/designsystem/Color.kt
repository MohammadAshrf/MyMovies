package com.example.core.presentation.designsystem

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

/* ---------- Brand ---------- */
private val MyMoviesPinkDark = Color(0xFFD85895)
private val MyMoviesPinkLight = Color(0xFFF564A9)
private val MyMoviesPinkSecondary = Color(0xFF64163B)
private val MyMoviesPinkSecondaryLight = Color(0xFF7D1C4A)

/* ---------- Dark theme base ---------- */
private val DarkBackground = Color(0xFF0D090B)
private val DarkSurface = Color(0xFF0D090B)
private val DarkSurfaceHigh = Color(0xFF141112)

private val DarkOnBackground = Color(0xDEFFFFFF)
private val DarkOnSurface = Color(0xDEFFFFFF)
private val DarkOnSurfaceVariant = Color(0x99FFFFFF)

/* ---------- Light theme base ---------- */
private val LightBackground = Color(0xFFFAF5F7)
private val LightSurface = Color(0xFFFFFFFF)

private val LightOnBackground = Color(0xDE1F1F1F)
private val LightOnSurface = Color(0xDE1F1F1F)
private val LightOnSurfaceVariant = Color(0x991F1F1F)

/* ---------- Material Color Schemes ---------- */

val DarkColorScheme = darkColorScheme(
    primary = MyMoviesPinkDark,
    secondary = MyMoviesPinkSecondary,
    background = DarkBackground,
    surface = DarkSurface,
    surfaceVariant = DarkSurfaceHigh,

    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = DarkOnBackground,
    onSurface = DarkOnSurface,
    onSurfaceVariant = DarkOnSurfaceVariant
)

val LightColorScheme = lightColorScheme(
    primary = MyMoviesPinkLight,
    secondary = MyMoviesPinkSecondaryLight,
    background = LightBackground,
    surface = LightSurface,
    surfaceVariant = LightBackground,

    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = LightOnBackground,
    onSurface = LightOnSurface,
    onSurfaceVariant = LightOnSurfaceVariant
)