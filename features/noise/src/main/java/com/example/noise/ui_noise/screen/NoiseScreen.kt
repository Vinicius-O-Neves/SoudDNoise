package com.example.noise.ui_noise.screen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.noise.ui_noise.components.AudioSpectrum
import com.example.noise.ui_noise.model.FrequencyState
import com.example.presentation.app.AppTheme
import com.example.presentation.sounddnoise.theme.SoundDNoiseTheme
import com.example.presentation.sounddnoise.theme.SoundDNoiseThemes

@Composable
fun NoiseScreen(frequenciesArray: FrequencyState) {
    Log.d("frequencies", frequenciesArray.toString())
    NoiseScreenContent(
        fastFourierTransformArray = frequenciesArray
    )
}

@Composable
private fun NoiseScreenContent(fastFourierTransformArray: FrequencyState) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .background(AppTheme.colors.background)
            .fillMaxSize()
    ) {
        AudioSpectrum(frequenciesArray = fastFourierTransformArray)
    }
}

@Composable
@SoundDNoiseThemes
private fun NoiseScreen_Previews() {
    SoundDNoiseTheme {
        NoiseScreen(
            FrequencyState(
                frequencies = floatArrayOf(1.2f, 190f, 12f, 0f, 25f)
            )
        )
    }
}