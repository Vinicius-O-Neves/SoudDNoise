package com.example.presentation.app.color

import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.ui.graphics.Color

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

val DarkColorScheme = darkColors(
    primary = Purple80,
    secondary = PurpleGrey80
)

val LightColorScheme = lightColors(
    primary = Purple40,
    secondary = PurpleGrey40
)


val appDarkColorPalette = AppColors(
    material = DarkColorScheme,
    primaryTextWarning = Pink40,
    onFeedbackDangerPrimary = Pink40,
    onFeedbackSuccessPrimary = Pink40,
    onFeedbackWarningPrimary = Pink40,
    onSurfacePrimary = Pink40
)

val appLightColorPalette = AppColors(
    material = LightColorScheme,
    primaryTextWarning = Pink40,
    onFeedbackDangerPrimary = Pink40,
    onFeedbackSuccessPrimary = Pink40,
    onFeedbackWarningPrimary = Pink40,
    onSurfacePrimary = Pink40
)