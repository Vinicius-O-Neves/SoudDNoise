package com.example.noise.ui_noise.viewmodel

import android.media.AudioRecord
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.noise.ui_noise.model.FrequencyState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.jtransforms.fft.DoubleFFT_1D
import java.nio.ByteBuffer
import kotlin.math.log10

const val SAMPLE_RATE = 44100 // Sample rate in Hz
const val FFT_SIZE = 256 // FFT size
const val BUFFER_SIZE = FFT_SIZE * 2// Buffer size in bytes

class NoiseActivityViewModel : ViewModel() {
    private var isRecording = false
    var audioRecord: AudioRecord? = null

    private val fft = DoubleFFT_1D(FFT_SIZE.toLong())

    var dbLevelsState = MutableStateFlow(FrequencyState(floatArrayOf(0f)))

    fun startRecording() {
        viewModelScope.launch {
            if (isRecording.not()) {
                isRecording = true
                audioRecord?.startRecording()
            }

            getDbLevels().catch {
                Log.d("frequencies", "error")
            }.collect { dbLevels ->
                setAudioDbLevels(levels = dbLevels)
            }
        }
    }

    private fun getDbLevels(): Flow<FloatArray> = flow {
        val buffer = ByteBuffer.allocateDirect(BUFFER_SIZE)
        val audioData = FloatArray(FFT_SIZE)
        val frequencies = FloatArray(FFT_SIZE / 2)
        val audioDataDouble = DoubleArray(FFT_SIZE * 2)

        while (isRecording) {
            audioRecord?.read(buffer, BUFFER_SIZE)

            for (i in 0 until FFT_SIZE) {
                audioData[i] = buffer.getShort(i * 2).toFloat() / Short.MAX_VALUE
            }

            fft.realForward(audioDataDouble.apply {
                for (i in audioData.indices) {
                    this[i] = audioData[i].toDouble()
                }
            })

            for (i in 0 until FFT_SIZE / 2) {
                val real = audioDataDouble[2 * i]
                val image = audioDataDouble[2 * i + 1]
                val magnitude = (real * real + image * image).toFloat()
                val db = if (magnitude > 0) 10 * log10(magnitude * magnitude) else 0f

                frequencies[i] = db
            }

            emit(frequencies)
        }
    }.flowOn(Dispatchers.Default)

    private fun setAudioDbLevels(levels: FloatArray) {
        dbLevelsState.value = dbLevelsState.value.copy(frequencies = levels)
    }

    fun stopRecording() {
        if (isRecording) {
            audioRecord?.stop()
            audioRecord?.release()
            isRecording = false
        }
    }

}