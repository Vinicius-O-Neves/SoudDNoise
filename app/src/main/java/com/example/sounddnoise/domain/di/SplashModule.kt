package com.example.sounddnoise.domain.di

import com.example.sounddnoise.viewmodel.SplashScreenViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.core.module.Module
import org.koin.dsl.module

fun injectSplashFeature() = loadFeature

private val loadFeature by lazy {
    loadKoinModules(
        listOf(
            viewModelModule
        )
    )
}

private val viewModelModule: Module = module {
    viewModel {
        SplashScreenViewModel()
    }
}