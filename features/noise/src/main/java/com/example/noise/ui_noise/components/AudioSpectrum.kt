package com.example.noise.ui_noise.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.dp
import com.example.noise.ui_noise.model.FrequencyState
import com.example.presentation.sounddnoise.theme.SoundDNoiseTheme
import com.example.presentation.sounddnoise.theme.SoundDNoiseThemes

@Composable
fun AudioSpectrum(modifier: Modifier = Modifier, frequenciesArray: FrequencyState) {
    Box(
        modifier = modifier
            .height(100.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        Canvas(
            modifier = Modifier.fillMaxSize(),
            onDraw = {
                frequenciesArray.frequencies.forEachIndexed { index, amplitude ->
                    val columnWidth = size.width / frequenciesArray.frequencies.size

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
    val startColor = getColorForAmplitude(amplitude - amplitude)
    val centerColor = getColorForAmplitude(amplitude - 10)
    val endColor = getColorForAmplitude(amplitude)

    drawRect(
        brush = Brush.verticalGradient(
            colors = listOf(
                endColor,
                centerColor,
                startColor
            ),
            startY = size.height - amplitude.toFloat(),
            endY = size.height
        ),
        topLeft = Offset(
            columnWidth * index,
            size.height - amplitude.toFloat()
        ),
        size = Size(columnWidth, amplitude.toFloat()),
    )
}

fun getColorForAmplitude(amplitude: Double): Color {
    return when {
        amplitude < 30.0 -> Color(0xFF07BB58)
        amplitude < 50.0 -> Color(0xFFD4FA00)
        amplitude < 70.0 -> Color(0xFFDF6203)
        amplitude < 100.0 -> Color(0xFFDF2F03)
        amplitude < 150.0 -> Color(0xFFB703DF)
        else -> Color(0xFF8303DF)
    }
}

@Composable
@SoundDNoiseThemes
fun AudioSpectrum_Previews() {
    SoundDNoiseTheme {
        AudioSpectrum(
            frequenciesArray = FrequencyState(
                frequencies = doubleArrayOf(1.2, 190.0, 12.2, 0.1, 25.0)
            )
        )
    }
}