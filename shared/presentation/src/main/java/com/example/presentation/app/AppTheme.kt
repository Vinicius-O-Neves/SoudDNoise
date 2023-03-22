package com.example.presentation.app

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import com.example.presentation.app.color.AppColors
import com.example.presentation.app.color.appLightColorPalette
import com.example.presentation.sounddnoise.theme.SoundDNoiseTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController

val LocalAppThemeComposition = compositionLocalOf {
    appLightColorPalette
}

object AppTheme {
    val colors: AppColors
        @Composable get() = LocalAppThemeComposition.current
}

@Composable
fun AppTheme(
    forceLightMode: Boolean = false,
    isSystemBarsVisible: Boolean = true,
    isStatusBarVisible: Boolean = true,
    content: @Composable () -> Unit
) {
    val systemUiController = rememberSystemUiController()

    val darkTheme = if (isSystemInDarkTheme()) {
        !forceLightMode
    } else {
        false
    }

    SoundDNoiseTheme(darkTheme = darkTheme) {
        content()
    }

    systemUiController.apply {
        setStatusBarColor(color = appLightColorPalette.primary)
        this.isSystemBarsVisible = isSystemBarsVisible
        this.isStatusBarVisible = isStatusBarVisible
    }

}