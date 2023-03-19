package com.example.sounddnoise.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class SplashScreenViewModel : ViewModel() {

    private val _redirect = MutableStateFlow(false)
    val redirect get() = _redirect

    fun canRedirectToMainActivity() {
        viewModelScope.launch {
            delay(5600)

            _redirect.value = true
        }
    }

}