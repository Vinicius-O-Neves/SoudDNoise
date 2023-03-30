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
import com.example.presentation.sounddnoise.theme.SoundDNoiseTheme
import com.example.presentation.sounddnoise.theme.SoundDNoiseThemes

@Composable
fun AudioSpectrum(modifier: Modifier = Modifier, frequenciesArray: DoubleArray) {
    Box(
        modifier = modifier
            .height(130.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        Canvas(
            modifier = Modifier.fillMaxSize(),
            onDraw = {
                frequenciesArray.forEachIndexed { index, amplitude ->
                    val columnWidth = size.width / frequenciesArray.size

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
            size.height - amplitude.toFloat() * 3
        ),
        size = Size(columnWidth, amplitude.toFloat() * 3),
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
        else -> Color(0xFF8303DF)
    }
}

@Composable
@SoundDNoiseThemes
fun AudioSpectrum_Previews() {
    SoundDNoiseTheme {
        AudioSpectrum(
            frequenciesArray = doubleArrayOf(1.2, 190.0, 12.2, 0.1, 25.0)
        )
    }
}