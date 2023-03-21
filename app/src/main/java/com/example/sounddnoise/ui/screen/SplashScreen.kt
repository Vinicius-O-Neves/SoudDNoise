package com.example.sounddnoise.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.presentation.app.AppTheme
import com.example.presentation.components.lottie.AppLottieAnimation
import com.example.sounddnoise.R

@Composable
fun SplashScreen() {
    SplashScreenContent()
}

@Composable
private fun SplashScreenContent() {
    AppLottieAnimation(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.colors.background),
        lottieFile = R.raw.ic_listening
    )
}

@Composable
@Preview
private fun SplashScreen_Preview() {
    AppTheme {
        SplashScreenContent()
    }
}