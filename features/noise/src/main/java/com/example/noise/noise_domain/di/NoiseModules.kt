package com.example.noise.noise_domain.di

import com.example.noise.ui_noise.viewmodel.NoiseActivityViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.core.module.Module
import org.koin.dsl.module

fun injectNoiseFeatures() = loadFeature

private val loadFeature by lazy {
    loadKoinModules(
        listOf(
            viewModelModule
        )
    )
}

private val viewModelModule: Module = module {
    viewModel {
        NoiseActivityViewModel()
    }
}