package com.example.sounddnoise.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.presentation.app.AppTheme
import androidx.compose.ui.tooling.preview.Preview
import com.example.sounddnoise.R

@Composable
fun SplashScreen() {
    SplashScreenContent()
}

@Composable
private fun SplashScreenContent() {
    val lottieComposition by rememberLottieComposition(
        spec = LottieCompositionSpec.RawRes(R.raw.ic_listening)
    )

    Surface(modifier = Modifier
        .fillMaxSize()
        .background(AppTheme.colors.primary)) {
        LottieAnimation(
            composition = lottieComposition,
            iterations = LottieConstants.IterateForever
        )
    }
}

@Composable
@Preview
private fun SplashScreen_Preview() {
    AppTheme {
        SplashScreenContent()
    }
}