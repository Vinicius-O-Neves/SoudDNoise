package com.example.presentation.app.color

import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.ui.graphics.Color

val ds_dark_blue = Color(0xFF05009C)
val ds_blue = Color(0xFF045DCA)
val ds_dark_blue_variant = Color(0xFF0600B8)

val ds_white = Color(0xFFE0DEDE)
val ds_full_white = Color(0xFFFFFFFF)

val ds_black = Color(0xFF2B2B2B)

val ds_gray = Color(0xFF777777)

val ds_green = Color(0xFF08BE35)

val ds_warning = Color(0xFFFF0000)

val DarkColorScheme = darkColors(
    primary = ds_dark_blue,
    secondary = ds_blue,
    onSecondary = ds_dark_blue_variant,
    background = ds_black,
    onBackground = ds_full_white
)

val LightColorScheme = lightColors(
    primary = ds_blue,
    secondary = ds_dark_blue,
    onSecondary = ds_dark_blue_variant,
    background = ds_white,
    onBackground = ds_black
)


val appDarkColorPalette = AppColors(
    material = DarkColorScheme,
    primaryTextWarning = ds_warning,
    onFeedbackDangerPrimary = ds_warning,
    onFeedbackSuccessPrimary = ds_green,
    onFeedbackWarningPrimary = ds_warning,
    onSurfacePrimary = ds_gray
)

val appLightColorPalette = AppColors(
    material = LightColorScheme,
    primaryTextWarning = ds_warning,
    onFeedbackDangerPrimary = ds_warning,
    onFeedbackSuccessPrimary = ds_green,
    onFeedbackWarningPrimary = ds_warning,
    onSurfacePrimary = ds_gray
)