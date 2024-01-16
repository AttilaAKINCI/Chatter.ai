package com.akinci.chatter.ui.ds.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

private val White_1 = Color(0xFFFFFFFF)
private val White_2 = Color(0xD9FFFFFF)
private val White_3 = Color(0xF2CCCCCC)

private val Blue_1 = Color(0xFF4189EF)
private val Blue_2 = Color(0xFF337BE2)

private val Grey_1 = Color(0xFFDDDDDD)
private val Grey_2 = Color(0xFF555555)
private val Grey_3 = Color(0xFF4f5055)
private val Grey_4 = Color(0xFF24242b)
private val Grey_5 = Color(0xFF131313)

private val Aqua = Color(0xFF00BCD4)
private val Teal = Color(0xFF009688)
private val Yellow_1 = Color(0xFFD1B814)

val LightColorScheme = lightColorScheme(
    primary = Blue_1,
    onPrimary = White_1,
    secondary = Grey_1,
    background = White_1,
    onBackground = Grey_5,
    surface = White_1,
    onSurface = Grey_5,
    surfaceVariant = White_2,
    onSurfaceVariant = Grey_5,
    tertiaryContainer = Aqua
)

val DarkColorScheme = darkColorScheme(
    primary = Blue_2,
    onPrimary = White_3,
    secondary = Grey_2,
    background = Grey_4,
    onBackground = White_3,
    surface = Grey_4,
    onSurface = White_3,
    surfaceVariant = Grey_3,
    onSurfaceVariant = White_3,
    tertiaryContainer = Teal
)

val Color.Companion.DarkYellow: Color get() = Yellow_1
