package com.example.noise.ui_noise.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.noise.ui_noise.model.FrequencyState
import com.example.presentation.app.AppTheme
import com.example.presentation.components.spacing.AppSpacing
import com.example.presentation.sounddnoise.theme.SoundDNoiseTheme
import com.example.presentation.sounddnoise.theme.SoundDNoiseThemes
import java.util.*

@Composable
fun AudioSpectrum(
    modifier: Modifier = Modifier,
    frequenciesState: () -> FrequencyState,
    audioDecibel: Double
) {
    val frequencies = frequenciesState().frequencies

    val formattedAudioDecibel =
        String.format(locale = Locale.getDefault(), format = "%.2f", audioDecibel)

    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Text(
            style = MaterialTheme.typography.h4.copy(
                color = AppTheme.colors.onBackground, fontWeight = FontWeight.Bold
            ),
            text = formattedAudioDecibel.plus(" Db"),
            modifier = Modifier.padding(bottom = 250.dp)
        )

        Canvas(
            modifier = Modifier
                .heightIn(max = AppSpacing.small)
                .fillMaxWidth(),
            onDraw = {
                frequencies.forEachIndexed { index, amplitude ->
                    val columnWidth = size.width / frequencies.size

                    audioSpectrumItem(
                        columnWidth = columnWidth,
                        amplitude = amplitude,
                        index = index
                    )
                }
            }
        )
    }
}

private fun DrawScope.audioSpectrumItem(
    columnWidth: Float,
    amplitude: Double,
    index: Int,
) {
    val gradientList = mutableListOf(getColorForAmplitude(10.0), getColorForAmplitude(20.0))

    for (i in 30..amplitude.toInt() step 10) {
        gradientList.add(0, getColorForAmplitude(i.toDouble()))
    }

    drawRect(
        brush = Brush.verticalGradient(
            colors = gradientList,
            startY = size.height - amplitude.toFloat(),
            endY = size.height
        ),
        topLeft = Offset(
            columnWidth * index,
            size.height - amplitude.toFloat() / 90
        ),
        size = Size(columnWidth, amplitude.toFloat() / 90),
    )
}

fun getColorForAmplitude(amplitude: Double): Color {
    return when {
        amplitude < 10.0 -> Color(0xFF088A43)
        amplitude < 20.0 -> Color(0xFF09C75F)
        amplitude < 30.0 -> Color(0xFFB9D80A)
        amplitude < 50.0 -> Color(0xFFD4FA00)
        amplitude < 60.0 -> Color(0xFFF7C306)
        amplitude < 70.0 -> Color(0xFFDF6203)
        amplitude < 100.0 -> Color(0xFFDF2F03)
        amplitude < 150.0 -> Color(0xFFB703DF)
        amplitude < 160.0 -> Color(0xFFA403DF)
        amplitude < 170.0 -> Color(0xFF5E0396)
        else -> Color(0xFF3A0808)
    }
}

@Composable
@SoundDNoiseThemes
fun AudioSpectrum_Previews() {
    SoundDNoiseTheme {
        AudioSpectrum(
            frequenciesState = {
                FrequencyState(
                    frequencies = doubleArrayOf(1.2, 190.0, 12.2, 0.1, 25.0)
                )
            },
            audioDecibel = 40.0
        )
    }
}