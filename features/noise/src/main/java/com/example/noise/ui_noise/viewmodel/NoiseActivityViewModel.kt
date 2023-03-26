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
import kotlin.math.sqrt

const val SAMPLE_RATE = 44100 // Sample rate in Hz
const val FFT_SIZE = 512 // FFT size
const val BUFFER_SIZE = FFT_SIZE * 2 // Buffer size in bytes

class NoiseActivityViewModel : ViewModel() {
    private var isRecording = false
    var audioRecord: AudioRecord? = null

    private val fft = DoubleFFT_1D(FFT_SIZE.toLong())
    private val dbRef = 32768.0 // Reference level for dBFS

    var dbLevelsState = MutableStateFlow(FrequencyState(doubleArrayOf(0.0)))

    fun startRecording() {
        viewModelScope.launch {
            if (isRecording.not()) {
                isRecording = true
                audioRecord?.startRecording()
            }

            while (isRecording) {
                getDbLevels().catch {
                    Log.d("frequencies", "error")
                }.collect { dbLevels ->
                    if (dbLevels.contentEquals(dbLevelsState.value.frequencies).not()) {
                        setAudioDbLevels(levels = dbLevels)
                    }
                }
            }
        }
    }

    private fun getDbLevels(): Flow<DoubleArray> = flow {
        val buffer = ByteBuffer.allocateDirect(BUFFER_SIZE)
        val audioData = DoubleArray(FFT_SIZE)
        val frequencies = DoubleArray(FFT_SIZE)
        val audioDataDouble = DoubleArray(FFT_SIZE * 2)

        val averageWindow = 80 // Number of samples to use in moving average
        val averageValues = DoubleArray(FFT_SIZE / 2)

        var dc = 0.0
        var count = 0

        while (isRecording) {
            audioRecord?.read(buffer, BUFFER_SIZE)

            for (i in 0 until FFT_SIZE) {
                audioData[i] = buffer.getShort(i * 2).toDouble() / Short.MAX_VALUE

                // Remove DC bias
                dc += audioData[i]
                count++
            }

            if (count == FFT_SIZE) {
                dc /= count
                for (i in 0 until FFT_SIZE) {
                    audioData[i] -= dc
                }
                count = 0
                dc = 0.0
            }

            fft.realForward(audioDataDouble.apply {
                for (i in audioData.indices) {
                    this[i] = audioData[i]
                }
            })

            for (i in 0 until FFT_SIZE / 2) {
                val real = audioDataDouble[2 * i]
                val image = audioDataDouble[2 * i + 1]
                val magnitude = sqrt(real * real + image * image).toFloat()

                // Calculate the dB level
                val dB = if (magnitude > 0) {
                    20 * log10(magnitude.toDouble())
                } else {
                    0.0
                }

                // Add the dB value to the moving average window
                averageValues[i] += (dB - averageValues[i]) / averageWindow
            }

            // Copy the averaged values into the output array
            System.arraycopy(averageValues, 0, frequencies, 0, averageValues.size)

            emit(frequencies)
        }
    }.flowOn(Dispatchers.Default)

    private fun setAudioDbLevels(levels: DoubleArray) {
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