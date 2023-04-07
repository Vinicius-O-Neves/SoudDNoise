package com.example.noise.ui_noise.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
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


        Column(
            verticalArrangement = Arrangement.Top,
            modifier = Modifier
                .padding(AppSpacing.base)
                .fillMaxWidth()
        ) {

            val warning = when (noiseState) {
                NoiseState.LOW -> R.string.warning_low
                NoiseState.MEDIUM -> R.string.warning_medium
                NoiseState.HIGH -> R.string.warning_high
            }

            Text(
                style = MaterialTheme.typography.h6.copy(
                    color = AppTheme.colors.onBackground
                ), text = stringResource(id = warning), textAlign = TextAlign.Center
            )

            val lottieAnimation = when (noiseState) {
                NoiseState.LOW -> R.raw.ic_calm_backdrop
                NoiseState.MEDIUM -> R.raw.ic_warning
                NoiseState.HIGH -> R.raw.ic_head_explosion
            }

            AppLottieAnimation(
                lottieFile = lottieAnimation,
                modifier = Modifier
                    .padding(top = AppSpacing.regular)
                    .heightIn(max = 300.dp)
            )
        }

        Box(contentAlignment = Alignment.BottomCenter, modifier = Modifier.weight(1f)) {
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