package com.example.sounddnoise.navigation

import android.content.Context
import android.content.Intent
import com.example.navigation.features.Feature

object SplashNavigation : Feature {

    private const val NOISE_ACTIVITY =
        "${Feature.MAIN_PACKAGE}noise.ui_noise.NoiseActivity"

    fun intentToNoiseActivity(context: Context): Intent? {
        return loadIntent(context, NOISE_ACTIVITY)
    }
}