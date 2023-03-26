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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.noise.ui_noise.model.FrequencyState
import com.example.presentation.app.AppTheme
import com.example.presentation.components.spacing.AppSpacing
import com.example.presentation.sounddnoise.theme.SoundDNoiseTheme
import com.example.presentation.sounddnoise.theme.SoundDNoiseThemes
import kotlin.math.pow
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
                text = "${frequenciesArray.frequencies.average().roundToLong()}".plus(" Db"),
            )
        }

        item {
            Spacer(modifier = Modifier.height(frequenciesArray.frequencies.max().dp / 3))
        }

        item {
            Row(
                modifier = Modifier.padding(top = AppSpacing.base)
            ) {
                Canvas(
                    modifier = Modifier
                        .weight(1f),
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
    val startColor = getColorForAmplitude(amplitude - amplitude)
    val centerColor = getColorForAmplitude(amplitude / 2)
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
        topLeft = Offset(columnWidth * index, size.height - amplitude.toFloat()),
        size = Size(columnWidth, amplitude.toFloat().pow(10))
    )
}

fun getColorForAmplitude(amplitude: Double): Color {
    return when {
        amplitude < 10.0 -> Color(0xFF013F57)
        amplitude < 20.0 -> Color(0xFF035E66)
        amplitude < 30.0 -> Color(0xFF058066)
        amplitude < 40.0 -> Color(0xFF079E7E)
        amplitude < 50.0 -> Color(0xFF07BB58)
        amplitude < 60.0 -> Color(0xFF82BE4D)
        amplitude < 70.0 -> Color(0xFF6FCA06)
        amplitude < 80.0 -> Color(0xFF7D9C09)
        amplitude < 90.0 -> Color(0xFFA3DF18)
        amplitude < 100.0 -> Color(0xFFD4FA00)
        amplitude < 110.0 -> Color(0xFFBBB207)
        amplitude < 120.0 -> Color(0xFFBB9D07)
        amplitude < 140.0 -> Color(0xFFE9AB0E)
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