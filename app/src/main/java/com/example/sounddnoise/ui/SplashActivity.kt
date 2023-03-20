package com.example.sounddnoise.ui

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import androidx.activity.compose.setContent
import androidx.lifecycle.Lifecycle
import com.example.presentation.app.AppTheme
import com.example.presentation.ui.BaseComponentActivity
import com.example.sounddnoise.ui.screen.SplashScreen
import com.example.sounddnoise.viewmodel.SplashScreenViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SplashActivity : BaseComponentActivity() {

    private val viewModel: SplashScreenViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                SplashScreen()
            }
        }
    }

    override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
        collectRedirectState()

        return super.onCreateView(name, context, attrs)
    }

    private fun collectRedirectState() {
        viewModel.canRedirectToMainActivity()

        collectFlow(
            flow = viewModel.redirect,
            lifecycleState = Lifecycle.State.RESUMED
        ) { redirect ->
            if (redirect) {
                TODO("NOT IMPLEMENT")
            }
        }
    }

    override fun setupViews() {}

}