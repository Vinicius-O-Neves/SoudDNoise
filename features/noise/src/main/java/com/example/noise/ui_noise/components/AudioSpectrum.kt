package com.example.noise.ui_noise.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import kotlin.math.roundToInt

@Composable
fun AudioSpectrum(frequenciesArray: FrequencyState) {
    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Text(
                style = MaterialTheme.typography.h4.copy(
                    color = AppTheme.colors.onBackground,
                    fontWeight = FontWeight.Bold
                ),
                text = frequenciesArray.frequencies.average().roundToInt().toString().plus(" Db"),
                modifier = Modifier
                    .padding(bottom = frequenciesArray.frequencies.max().dp / 3)

            )
        }

        item {
            Row(
                modifier = Modifier.padding(top = AppSpacing.base),
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.Center
            ) {
                Canvas(
                    modifier = Modifier.fillMaxWidth(),
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
    amplitude: Float,
    index: Int,
) {
    val startColor = getColorForAmplitude(amplitude - amplitude)
    val nextStartColor = getColorForAmplitude(amplitude - amplitude / 2)
    val centerColor = getColorForAmplitude(amplitude - amplitude / 1.7f)
    val nearCenterColor = getColorForAmplitude(amplitude - amplitude / 1.5f)
    val nextCenterColor = getColorForAmplitude(amplitude - 10)
    val endColor = getColorForAmplitude(amplitude)

    drawRect(
        brush = Brush.verticalGradient(
            colors = listOf(
                endColor,
                nextCenterColor,
                nearCenterColor,
                centerColor,
                nextStartColor,
                startColor
            ),
            startY = size.height - amplitude,
            endY = size.height
        ),
        topLeft = Offset(columnWidth * index / 1.2f, size.height - amplitude),
        size = Size(columnWidth / 1.2f, amplitude)
    )
}

fun getColorForAmplitude(amplitude: Float): Color {
    return when {
        amplitude < 10f -> Color(0xFF013F57)
        amplitude < 20f -> Color(0xFF035E66)
        amplitude < 30f -> Color(0xFF058066)
        amplitude < 40f -> Color(0xFF079E7E)
        amplitude < 50f -> Color(0xFF07BB58)
        amplitude < 60f -> Color(0xFF82BE4D)
        amplitude < 70f -> Color(0xFF6FCA06)
        amplitude < 80f -> Color(0xFF7D9C09)
        amplitude < 90f -> Color(0xFFA3DF18)
        amplitude < 100f -> Color(0xFFD4FA00)
        amplitude < 110f -> Color(0xFFBBB207)
        amplitude < 120f -> Color(0xFFBB9D07)
        amplitude < 140f -> Color(0xFFE9AB0E)
        amplitude < 150f -> Color(0xFFDF6203)
        amplitude < 170f -> Color(0xFFDF2F03)
        amplitude < 180f -> Color(0xFFB703DF)
        else -> Color(0xFF8303DF)
    }
}

@Composable
@SoundDNoiseThemes
fun AudioSpectrum_Previews() {
    SoundDNoiseTheme {
        AudioSpectrum(
            FrequencyState(
                frequencies = floatArrayOf(1.2f, 190f, 12f, 0f, 25f)
            )
        )
    }
}