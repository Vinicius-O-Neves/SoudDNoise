package com.example.presentation.ui

import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity

abstract class BaseActivity : AppCompatActivity, ObservableLifecycleOwner {

    constructor() : super()
    constructor(@LayoutRes res: Int) : super(res)

    abstract fun setupViews()

}