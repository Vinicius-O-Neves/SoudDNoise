package com.example.noise.ui_noise

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import com.example.noise.ui_noise.screen.NoiseScreen
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
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            initAudioRecord()
            viewModel.startRecording()
        }
    }

    override fun onDestroy() {
        viewModel.stopRecording()
        super.onDestroy()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                NoiseScreen(
                    frequenciesArray = viewModel.dbLevelsState.value
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
            registerPermission.launch(Manifest.permission.RECORD_AUDIO)
        } else {
            initAudioRecord()
        }

        return super.onCreateView(name, context, attrs)
    }

    private fun initAudioRecord() {
        lifecycleScope.launch {
            val minBufferSize = AudioRecord.getMinBufferSize(
                SAMPLE_RATE, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT
            )

            var audioRecord: AudioRecord? = null

            if (ActivityCompat.checkSelfPermission(
                    this@NoiseActivity,
                    Manifest.permission.RECORD_AUDIO
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                audioRecord = AudioRecord(
                    MediaRecorder.AudioSource.MIC,
                    SAMPLE_RATE,
                    AudioFormat.CHANNEL_IN_MONO,
                    AudioFormat.ENCODING_PCM_16BIT,
                    minBufferSize
                )
            }

            viewModel.audioRecord = audioRecord

            delay(1000)

            viewModel.startRecording()
        }
    }

    override fun setupViews() {}

}