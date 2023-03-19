package com.example.presentation.sounddnoise.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Shapes
import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.dp

@Immutable
object SoundDNoiseRoundedCornerSize {
    val mini = 4.dp
    val medium = 6.dp
    val medium_large = 8.dp
    val base = 12.dp
    val large = 16.dp
    val xlarge = 20.dp
}

val ShapeFlat = RoundedCornerShape(
    topStartPercent = 0, topEndPercent = 0, bottomEndPercent = 0, bottomStartPercent = 0
)

val ShapeRectUpperCircleBottom =
    RoundedCornerShape(
        topStartPercent = 0,
        topEndPercent = 0,
        bottomStartPercent = 12,
        bottomEndPercent = 12
    )

val SoundDNoiseShapes = Shapes(
    small = RoundedCornerShape(SoundDNoiseRoundedCornerSize.mini),
    medium = RoundedCornerShape(SoundDNoiseRoundedCornerSize.base),
    large = RoundedCornerShape(SoundDNoiseRoundedCornerSize.xlarge)
)