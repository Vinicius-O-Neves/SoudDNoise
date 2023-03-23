package com.example.noise.ui_noise.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.presentation.app.AppTheme
import com.example.presentation.components.spacing.AppSpacing
import com.example.presentation.sounddnoise.theme.SoundDNoiseTheme
import com.example.presentation.sounddnoise.theme.SoundDNoiseThemes
import kotlin.math.roundToInt

@Composable
fun AudioSpectrum(fastFourierTransformArray: FloatArray) {
    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround
    ) {
        item {
            Text(
                style = MaterialTheme.typography.h5.copy(
                    color = AppTheme.colors.onBackground,
                    fontWeight = FontWeight.Bold
                ),
                text = fastFourierTransformArray.average().roundToInt().toString().plus(" Db"),
                modifier = Modifier
                    .padding(bottom = (fastFourierTransformArray.max().dp - 20.dp) / 3)

            )
        }

        item {
            Row(
                modifier = Modifier.padding(top = AppSpacing.base),
                verticalAlignment = Alignment.Bottom
            ) {
                Canvas(
                    modifier = Modifier
                        .width(100.dp),
                    onDraw = {
                        fastFourierTransformArray.forEachIndexed { index, amplitude ->
                            val columnWidth = size.width / fastFourierTransformArray.size

                            audioSpectrumItem(
                                color = getColorForAmplitude(amplitude),
                                columnWidth = columnWidth,
                                rowHeight = amplitude,
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
    color: Color,
    columnWidth: Float,
    rowHeight: Float,
    index: Int,
) {
    drawRect(
        color = color,
        topLeft = Offset(columnWidth * index, size.height - rowHeight),
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

@Composable
@SoundDNoiseThemes
fun AudioSpectrum_Preview() {
    SoundDNoiseTheme {
        AudioSpectrum(floatArrayOf(30f, 50f, 120f, 85f, 100f, 10f, 30f))
    }
}