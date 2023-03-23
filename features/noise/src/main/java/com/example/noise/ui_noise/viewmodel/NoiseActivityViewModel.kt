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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import org.jtransforms.fft.DoubleFFT_1D
import java.lang.StrictMath.sqrt
import java.nio.ByteBuffer

const val SAMPLE_RATE = 44100 // Sample rate in Hz
const val FFT_SIZE = 1024 // FFT size
const val BUFFER_SIZE = FFT_SIZE * 2 // Buffer size in bytes

class AudioRecordViewModel : ViewModel() {
    private var isRecording = false
    private lateinit var audioRecord: AudioRecord

    private val fft = DoubleFFT_1D(FFT_SIZE.toLong())
    private val _frequencies = MutableStateFlow(floatArrayOf())
    val frequencies get() = _frequencies

    fun startRecording(context: Context) {
        if (isRecording) return

        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.RECORD_AUDIO
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            audioRecord = AudioRecord(
                MediaRecorder.AudioSource.MIC,
                SAMPLE_RATE,
                AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT,
                BUFFER_SIZE
            )
        }


        isRecording = true
        viewModelScope.launch {
            recordAudioAndEmitFrequencies().collect {
                _frequencies.value = it
            }
        }
    }

    fun stopRecording() {
        if (!isRecording) return

        audioRecord.stop()
        audioRecord.release()
        isRecording = false
    }

    private fun recordAudioAndEmitFrequencies(): Flow<FloatArray> = flow {
        val buffer = ByteBuffer.allocateDirect(BUFFER_SIZE)

        while (isRecording) {
            audioRecord.read(buffer, BUFFER_SIZE)
            val audioData = FloatArray(FFT_SIZE)

            for (i in 0 until FFT_SIZE) {
                audioData[i] = buffer.getShort(i * 2).toFloat() / Short.MAX_VALUE
            }

            val audioDataDouble = audioData.map { it.toDouble() }.toDoubleArray()
            fft.realForward(audioDataDouble)

            val frequencies = FloatArray(FFT_SIZE / 2)
            for (i in 0 until FFT_SIZE / 2) {
                val re = audioDataDouble[2 * i]
                val im = audioDataDouble[2 * i + 1]
                val magnitude = sqrt(re * re + im * im).toFloat()

                frequencies[i] = magnitude
            }

            emit(frequencies)
        }
    }.flowOn(Dispatchers.IO)

    fun isRecording(): Boolean = isRecording

    override fun onCleared() {
        audioRecord.stop()
        audioRecord.release()
        super.onCleared()
    }
}