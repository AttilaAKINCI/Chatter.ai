package com.akinci.chatter.ui.ds.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

private val White_1 = Color(0xFFFFFFFF)

private val Blue_1 = Color(0xFF4189EF)
private val Blue_2 = Color(0xFF337BE2)

private val Grey_1 = Color(0xFFF0F0F0)
private val Grey_2 = Color(0xFFDDDDDD)
private val Grey_3 = Color(0xF2CCCCCC)
private val Grey_4 = Color(0xF2999999)
private val Grey_5 = Color(0xFF555555)
private val Grey_6 = Color(0xFF4F5055)
private val Grey_7 = Color(0xFF24242b)
private val Grey_8 = Color(0xFF131313)

private val Green_1 = Color(0xFFE6FFDB)
private val Green_2 = Color(0xFF015C4B)

private val Aqua = Color(0xFF00BCD4)
private val Teal = Color(0xFF009688)
private val Yellow_1 = Color(0xFFD1B814)

val LightColorScheme = lightColorScheme(
    primary = Blue_1,
    onPrimary = White_1,
    secondary = Grey_2,
    background = White_1,
    onBackground = Grey_8,
    surface = White_1,
    onSurface = Grey_8,
    surfaceVariant = Grey_1,
    onSurfaceVariant = Grey_4,
    tertiaryContainer = Green_1
)

val DarkColorScheme = darkColorScheme(
    primary = Blue_2,
    onPrimary = Grey_3,
    secondary = Grey_5,
    background = Grey_7,
    onBackground = Grey_3,
    surface = Grey_7,
    onSurface = Grey_3,
    surfaceVariant = Grey_6,
    onSurfaceVariant = Grey_4,
    tertiaryContainer = Green_2
)

val Color.Companion.DarkYellow: Color get() = Yellow_1
