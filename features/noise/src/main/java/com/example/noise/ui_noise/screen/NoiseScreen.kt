package com.example.noise.ui_noise.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.noise.ui_noise.NoiseState
import com.example.noise.ui_noise.components.AudioSpectrum
import com.example.noise.ui_noise.model.FrequencyState
import com.example.presentation.app.AppTheme
import com.example.presentation.components.lottie.AppLottieAnimation
import com.example.presentation.components.spacing.AppSpacing
import com.example.presentation.sounddnoise.theme.SoundDNoiseTheme
import com.example.presentation.sounddnoise.theme.SoundDNoiseThemes
import com.example.sounddnoise.R

@Composable
fun NoiseScreen(frequencyState: FrequencyState, audioDecibel: Double, noiseState: NoiseState) {
    NoiseScreenContent(
        frequenciesState = { frequencyState }, audioDecibel = audioDecibel, noiseState = noiseState
    )
}

@Composable
private fun NoiseScreenContent(
    frequenciesState: () -> FrequencyState, audioDecibel: Double, noiseState: NoiseState
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .background(AppTheme.colors.background)
            .fillMaxSize()
    ) {
        val lottieAnimation = when (noiseState) {
            NoiseState.LOW -> R.raw.ic_calm_backdrop
            NoiseState.MEDIUM -> R.raw.ic_warning
            NoiseState.HIGH -> R.raw.ic_head_explosion
        }

        AppLottieAnimation(
            lottieFile = lottieAnimation,
            modifier = Modifier
                .fillMaxWidth()
                .height(500.dp)
                .padding(AppSpacing.base)
        )

        Spacer(modifier = Modifier.weight(1f))

        Column {
            AudioSpectrum(
                frequenciesState = frequenciesState, audioDecibel = audioDecibel
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
                frequencies = doubleArrayOf(1.2, 190.0, 12.2, 0.1, 25.0),
            ), audioDecibel = 40.023214, noiseState = NoiseState.HIGH
        )
    }
}