package com.example.noise.ui_noise.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.tooling.preview.Preview
import com.example.presentation.app.AppTheme

@Composable
fun AudioSpectrum(fastFourierTransformArray: FloatArray) {
    Canvas(
        modifier = Modifier.fillMaxSize(),
        onDraw = {
            val width = size.width
            val height = size.height / 3
            val rowHeight = height / fastFourierTransformArray.size

            fastFourierTransformArray.forEachIndexed { index, amplitude ->
                val columnWidth = width / fastFourierTransformArray.size

                audioSpectrumItem(
                    color = getColorForAmplitude(amplitude),
                    columnWidth = columnWidth,
                    rowHeight = rowHeight,
                    index = index
                )
            }
        }
    )
}

private fun DrawScope.audioSpectrumItem(
    color: Color,
    columnWidth: Float,
    rowHeight: Float,
    index: Int
) {
    drawRect(
        color = color,
        topLeft = Offset(columnWidth * index, rowHeight - index),
        size = Size(columnWidth, rowHeight)
    )
}

fun getColorForAmplitude(amplitude: Float): Color {
    return when {
        amplitude < 20f -> Color.Blue
        amplitude < 40f -> Color.Cyan
        amplitude < 60f -> Color.Green
        amplitude < 80f -> Color.Yellow
        amplitude < 100f -> Color.Magenta
        else -> Color.Red
    }
}

@Preview
@Composable
fun AudioSpectrum_Preview() {
    AppTheme {
        AudioSpectrum(floatArrayOf(1.2f, 1.4f, 0f, 2.6f))
    }
}