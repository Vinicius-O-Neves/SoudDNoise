package com.example.presentation.sounddnoise.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import com.example.presentation.app.LocalAppThemeComposition
import com.example.presentation.app.color.appDarkColorPalette
import com.example.presentation.app.color.appLightColorPalette

val soundDNoisePaletteLight = appLightColorPalette
val soundDNoisePaletteDark = appDarkColorPalette

@Composable
fun SoundDNoiseTheme(
    darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        soundDNoisePaletteDark
    } else {
        soundDNoisePaletteLight
    }

    CompositionLocalProvider(LocalAppThemeComposition provides colors) {
        MaterialTheme(
            content = content, typography = MaterialTheme.typography, shapes = SoundDNoiseShapes
        )
    }
}