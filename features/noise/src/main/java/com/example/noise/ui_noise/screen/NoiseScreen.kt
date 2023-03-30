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
import java.util.*

@Composable
fun NoiseScreen(frequencyState: FrequencyState, audioDecibel: Double) {
    NoiseScreenContent(
        frequenciesState = frequencyState,
        audioDecibel = audioDecibel
    )
}

@Composable
private fun NoiseScreenContent(frequenciesState: FrequencyState, audioDecibel: Double) {
    val formattedAudioDecibel =
        String.format(locale = Locale.getDefault(), format = "%.2f", audioDecibel)

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
                text = formattedAudioDecibel.plus(" Db"),
            )

            Spacer(
                modifier = Modifier
                    .height(AppSpacing.xlarge)
            )

            AudioSpectrum(
                frequenciesArray = frequenciesState.frequencies
            )
        }
    }
}

@Composable
@SoundDNoiseThemes
private fun NoiseScreen_Previews() {
    SoundDNoiseTheme {
        NoiseScreen(
            frequencyState = FrequencyState(
                frequencies = doubleArrayOf(1.2, 190.0, 12.2, 0.1, 25.0)
            ),
            audioDecibel = 40.023214
        )
    }
}