package com.example.noise.ui_noise

import android.os.Bundle
import androidx.activity.compose.setContent
import com.example.noise.ui_noise.screen.NoiseScreen
import com.example.presentation.app.AppTheme
import com.example.presentation.ui.BaseActivity

class NoiseActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                NoiseScreen()
            }
        }
    }

    override fun setupViews() {}

}