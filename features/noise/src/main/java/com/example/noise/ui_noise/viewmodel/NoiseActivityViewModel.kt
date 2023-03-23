package com.example.noise.ui_noise.viewmodel

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import org.jtransforms.fft.FloatFFT_1D
import java.lang.StrictMath.abs
import java.lang.StrictMath.log10

class AudioRecordViewModel : ViewModel() {

    companion object {
        const val SAMPLE_RATE = 44100 // audio sample rate in Hz
        const val CHANNEL_CONFIG = AudioFormat.CHANNEL_IN_MONO // audio channel configuration
        const val AUDIO_ENCODING = AudioFormat.ENCODING_PCM_16BIT // audio encoding format
    }

    private val _audioBufferSize =
        AudioRecord.getMinBufferSize(
            SAMPLE_RATE,
            CHANNEL_CONFIG,
            AUDIO_ENCODING
        ) * 2 // audio buffer size in bytes

    private var _audioRecord: AudioRecord? = null

    private val _amplitudes = MutableStateFlow(0f)
    val amplitudes get() = _amplitudes

    private val _fftArray = flow {
        val buffer = FloatArray(_audioBufferSize / 2)
        while (true) {
            val fft = calculateFFT(buffer)
            emit(fft)
        }
    }.flowOn(Dispatchers.IO)
    val fftArray get() = _fftArray

    private fun startRecord() {
        viewModelScope.launch {
            _audioRecord?.startRecording()

            viewModelScope.launch {
                while (true) {
                    val buffer = ShortArray(_audioBufferSize / 2)
                    val amplitude = calculateAmplitude(buffer)
                    _amplitudes.value = amplitude
                }
            }
        }
    }

    private fun setAudioRecord(context: Context) {
        _audioRecord = if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.RECORD_AUDIO
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            AudioRecord(
                MediaRecorder.AudioSource.MIC,
                SAMPLE_RATE,
                CHANNEL_CONFIG,
                AUDIO_ENCODING,
                _audioBufferSize
            )
        } else {
            AudioRecord(
                MediaRecorder.MEDIA_RECORDER_INFO_UNKNOWN,
                SAMPLE_RATE,
                CHANNEL_CONFIG,
                AUDIO_ENCODING,
                _audioBufferSize
            )
        }
    }

    private fun calculateAmplitude(buffer: ShortArray): Float {
        var sum = 0.0
        for (sample in buffer) {
            sum += abs(sample.toDouble())
        }
        val rms = sum / buffer.size
        val db = if (rms > 0) 20 * log10(rms) else -Float.MAX_VALUE
        return db.toFloat()
    }

    private fun calculateFFT(buffer: FloatArray): FloatArray {
        val fft = FloatFFT_1D(buffer.size.toLong())
        fft.realForward(buffer)
        return buffer
    }

    override fun onCleared() {
        _audioRecord?.stop()
        _audioRecord?.release()
        super.onCleared()
    }
}