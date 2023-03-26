package com.example.noise.ui_noise.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import com.example.noise.ui_noise.model.FrequencyState
import com.example.presentation.app.AppTheme
import com.example.presentation.components.spacing.AppSpacing
import com.example.presentation.sounddnoise.theme.SoundDNoiseTheme
import com.example.presentation.sounddnoise.theme.SoundDNoiseThemes
import kotlin.math.roundToLong

@Composable
fun AudioSpectrum(modifier: Modifier = Modifier, frequenciesArray: FrequencyState) {
    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        item {
            Text(
                style = MaterialTheme.typography.h4.copy(
                    color = AppTheme.colors.onBackground,
                    fontWeight = FontWeight.Bold
                ),
                text = "${frequenciesArray.average}".plus(" Db"),
            )
        }

        item {
            Spacer(modifier = Modifier.height(frequenciesArray.frequencies.max().dp / 1.7f))
        }

        item {
            val screenWidth = LocalConfiguration.current.screenWidthDp.dp
            val screenHeight = LocalConfiguration.current.screenHeightDp.dp
            val canvasSize = min(screenWidth, screenHeight) - AppSpacing.base * 2

            Box(
                modifier = modifier
                    .padding(horizontal = AppSpacing.base)
                    .height(canvasSize),
                contentAlignment = Alignment.Center
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
    }
}

private fun DrawScope.audioSpectrumItem(
    columnWidth: Float,
    amplitude: Double,
    index: Int,
) {
    val scaledAmplitude = (amplitude * 10).toFloat()

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
            startY = size.height - scaledAmplitude,
            endY = size.height
        ),
        topLeft = Offset(
            columnWidth * index,
            size.height - scaledAmplitude / 2
        ),
        size = Size(columnWidth, scaledAmplitude),
    )
}

fun getColorForAmplitude(amplitude: Double): Color {
    return when {
        amplitude < 50.0 -> Color(0xFF07BB58)
        amplitude < 100.0 -> Color(0xFFD4FA00)
        amplitude < 150.0 -> Color(0xFFDF6203)
        amplitude < 170.0 -> Color(0xFFDF2F03)
        amplitude < 180.0 -> Color(0xFFB703DF)
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