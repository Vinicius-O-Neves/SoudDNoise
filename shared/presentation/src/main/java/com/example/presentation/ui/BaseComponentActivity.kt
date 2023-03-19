package com.example.presentation.ui

import androidx.appcompat.app.AppCompatActivity

abstract class BaseComponentActivity : AppCompatActivity(), ObservableLifecycleOwner {

    abstract fun setupViews()

}