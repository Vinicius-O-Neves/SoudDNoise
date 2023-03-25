package com.example.sounddnoise.app

import android.app.Application
import com.example.noise.noise_domain.di.injectNoiseFeatures
import com.example.sounddnoise.domain.di.injectSplashFeature
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class SoundDNoiseApp : Application() {

    override fun onCreate() {
        super.onCreate()

        initKoin()
    }

    private fun initKoin() {
        startKoin { androidContext(this@SoundDNoiseApp) }

        initAppFeatures()
    }

    private fun initAppFeatures() {
        injectSplashFeature()
        injectNoiseFeatures()
    }

}