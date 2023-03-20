package com.example.presentation.components.bottomsheet

import androidx.compose.foundation.shape.RoundedCornerShape
import com.example.presentation.sounddnoise.theme.ShapeFlat
import com.example.presentation.sounddnoise.theme.SoundDNoiseRoundedCornerSize

val BottomSheetShapeTopRounded = RoundedCornerShape(
    topStartPercent = SoundDNoiseRoundedCornerSize.medium.value.toInt(),
    topEndPercent = SoundDNoiseRoundedCornerSize.medium.value.toInt(),
    bottomEndPercent = 0,
    bottomStartPercent = 0
)

val BottomSheetShapeFlat = ShapeFlat