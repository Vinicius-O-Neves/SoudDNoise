package com.example.noise.ui_noise

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.collectAsState
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import com.example.noise.ui_noise.screen.NoiseScreen
import com.example.noise.ui_noise.viewmodel.BUFFER_SIZE
import com.example.noise.ui_noise.viewmodel.NoiseActivityViewModel
import com.example.noise.ui_noise.viewmodel.SAMPLE_RATE
import com.example.presentation.app.AppTheme
import com.example.presentation.ui.BaseComponentActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class NoiseActivity : BaseComponentActivity() {

    private val viewModel: NoiseActivityViewModel by viewModel()

    private val registerPermission = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val allPermissionsGranted = permissions.entries.all {
            it.value
        }

        if (allPermissionsGranted) {
            viewModel.initLocation(this)

            initAudioRecord()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.stopRecording()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                NoiseScreen(
                    frequencyState = viewModel.frequenciesState.collectAsState().value,
                    audioDecibel = viewModel.audioDecibel.collectAsState().value,
                    noiseState = viewModel.noiseState.collectAsState().value,
                    onHelpClick = ::onHelpClick
                )
            }
        }
    }

    override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.RECORD_AUDIO
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            registerPermission.launch(
                arrayOf(
                    Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        } else {
            initAudioRecord()

            viewModel.initLocation(this)
        }

        return super.onCreateView(name, context, attrs)
    }

    @SuppressLint("MissingPermission")
    private fun initAudioRecord() {
        lifecycleScope.launch {
            viewModel.audioRecord = AudioRecord(
                MediaRecorder.AudioSource.MIC,
                SAMPLE_RATE,
                AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT,
                BUFFER_SIZE
            )

            delay(100)

            viewModel.startRecording()
        }
    }

    private fun onHelpClick() {
        viewModel.fetchLastLocation()
    }

    override fun setupViews() {}

}