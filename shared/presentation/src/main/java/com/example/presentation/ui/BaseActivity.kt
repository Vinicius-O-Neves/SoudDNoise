package com.example.presentation.ui

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity

abstract class BaseActivity : AppCompatActivity, ObservableLifecycleOwner {

    constructor() : super()
    constructor(@LayoutRes res: Int) : super(res)

    override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
        setupViews()
        return super.onCreateView(name, context, attrs)
    }

    abstract fun setupViews()

}