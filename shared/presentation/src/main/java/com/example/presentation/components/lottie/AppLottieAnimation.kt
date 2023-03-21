package com.example.presentation.components.lottie

import androidx.annotation.RawRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.presentation.app.AppTheme

@Composable
fun AppLottieAnimation(modifier: Modifier = Modifier, @RawRes lottieFile: Int) {
    val lottieComposition by rememberLottieComposition(
        spec = LottieCompositionSpec.RawRes(lottieFile)
    )

    Box(modifier = modifier) {
        LottieAnimation(
            composition = lottieComposition,
            iterations = LottieConstants.IterateForever
        )
    }
}