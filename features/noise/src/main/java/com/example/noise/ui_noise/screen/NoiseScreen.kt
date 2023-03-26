package com.example.noise.ui_noise.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.example.noise.ui_noise.components.AudioSpectrum
import com.example.noise.ui_noise.model.FrequencyState
import com.example.presentation.app.AppTheme
import com.example.presentation.components.spacing.AppSpacing
import com.example.presentation.sounddnoise.theme.SoundDNoiseTheme
import com.example.presentation.sounddnoise.theme.SoundDNoiseThemes

@Composable
fun NoiseScreen(frequenciesArray: FrequencyState) {
    NoiseScreenContent(
        fastFourierTransformArray = frequenciesArray
    )
}

@Composable
private fun NoiseScreenContent(fastFourierTransformArray: FrequencyState) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .background(AppTheme.colors.background)
            .fillMaxSize()
    ) {
        Spacer(modifier = Modifier.weight(1f))

        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(bottom = AppSpacing.base)
                .fillMaxWidth()
        ) {
            Text(
                style = MaterialTheme.typography.h4.copy(
                    color = AppTheme.colors.onBackground,
                    fontWeight = FontWeight.Bold
                ),
                text = "${fastFourierTransformArray.average}".plus(" Db"),
            )

            Spacer(modifier = Modifier
                .height(AppSpacing.xlarge))

            AudioSpectrum(
                frequenciesArray = fastFourierTransformArray
            )
        }
    }
}

@Composable
@SoundDNoiseThemes
private fun NoiseScreen_Previews() {
    SoundDNoiseTheme {
        NoiseScreen(
            FrequencyState(
                frequencies = doubleArrayOf(1.2, 190.0, 12.2, 0.1, 25.0)
            )
        )
    }
}