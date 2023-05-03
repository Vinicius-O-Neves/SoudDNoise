package com.example.noise.ui_noise.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
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
fun NoiseScreen(
    frequencyState: FrequencyState,
    audioDecibel: Double,
    noiseState: NoiseState,
    onHelpClick: () -> Unit
) {
    NoiseScreenContent(
        frequenciesState = { frequencyState },
        audioDecibel = audioDecibel,
        noiseState = noiseState,
        onHelpClick = onHelpClick
    )
}

@Composable
private fun NoiseScreenContent(
    frequenciesState: () -> FrequencyState,
    audioDecibel: Double,
    noiseState: NoiseState,
    onHelpClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .background(AppTheme.colors.background)
            .fillMaxSize()
    ) {


        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
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
                    .padding(top = AppSpacing.small)
                    .heightIn(max = 300.dp)
            )

            OutlinedButton(
                onClick = { onHelpClick() },
                border = BorderStroke(1.dp, Color.Black),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color.Red,
                    contentColor = AppTheme.colors.error
                ),
                modifier = Modifier.padding(top = AppSpacing.regular),
                content = {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Text(
                            style = MaterialTheme.typography.h6.copy(
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            ),
                            text = "Help",
                        )
                    }
                }
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
            ), audioDecibel = 40.023214, noiseState = NoiseState.HIGH,
            onHelpClick = {}
        )
    }
}