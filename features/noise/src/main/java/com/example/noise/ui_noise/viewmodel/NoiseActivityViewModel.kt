package com.example.noise.ui_noise.viewmodel

import android.content.Context
import android.media.AudioRecord
import android.os.CountDownTimer
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.noise.ui_noise.model.FrequencyState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.jtransforms.fft.DoubleFFT_1D
import java.nio.ByteBuffer
import kotlin.math.log10
import kotlin.math.pow
import kotlin.math.sqrt

const val SAMPLE_RATE = 24100 // Sample rate in Hz
const val FFT_SIZE = 512 // FFT size
const val BUFFER_SIZE = FFT_SIZE // Buffer size in bytes

class NoiseActivityViewModel : ViewModel() {
    private var isRecording = false
    var audioRecord: AudioRecord? = null

    private val fft = DoubleFFT_1D(FFT_SIZE.toLong())
    private val audioDataDouble = DoubleArray(FFT_SIZE * 2)
    private val magnitudes = DoubleArray(FFT_SIZE)
    private val amplitudes = DoubleArray(FFT_SIZE / 2)

    var dbLevelsState = MutableStateFlow(FrequencyState(doubleArrayOf(0.0)))

    private var _dbAverage = mutableStateOf(0)
    val dbAverage get() = _dbAverage

    private var _previousDbAverage = 0

    private var dbAverageDownloadCountDown: CountDownTimer? = null
    private val countDownInterval = 1000L

    companion object {
        const val MAX_TIME_TO_WAIT_FOR_ANALYSES = 1
    }


    fun startRecording() {
        viewModelScope.launch {
            if (isRecording.not()) {
                isRecording = true
                audioRecord?.startRecording()
            }

            getMagnitudesFromRecordingAudio().catch {
                Log.d("frequencies", "error")
            }.collect { magnitudes ->
                setAudioAmplitudes(magnitudes = magnitudes)
            }
        }
    }

    private fun getMagnitudesFromRecordingAudio(): Flow<DoubleArray> = flow {
        val buffer = ByteBuffer.allocateDirect(BUFFER_SIZE)
        val audioData = DoubleArray(FFT_SIZE)

        var dc = 0.0
        var count = 0

        while (isRecording) {
            buffer.rewind()
            audioRecord?.read(buffer, FFT_SIZE * 2)

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
                val magnitude = sqrt(real * real + image * image)
                magnitudes[i] = magnitude
            }

            emit(magnitudes)
        }
    }.flowOn(Dispatchers.Default)

    private fun setAudioAmplitudes(magnitudes: DoubleArray) {
        for (i in amplitudes.indices) {
            val magnitude = magnitudes[i]
            if (magnitude <= 0.0) {
                amplitudes[i] = -magnitude
            } else {
                val db = 20 * log10(magnitude)
                amplitudes[i] = 10.0.pow(db / 20.0)
            }
        }

        dbLevelsState.value = dbLevelsState.value.copy(
            frequencies = amplitudes.sliceArray(0 until 6),
        )

        if (dbAverageDownloadCountDown == null) {
            startDbAverageDownloadCountDown()
            _dbAverage.value = amplitudes.average().toInt()
        }
    }

    private fun startDbAverageDownloadCountDown() {
        dbAverageDownloadCountDown = null

        dbAverageDownloadCountDown =
            object : CountDownTimer(countDownInterval * MAX_TIME_TO_WAIT_FOR_ANALYSES, countDownInterval) {
                override fun onFinish() {
                    if (_previousDbAverage != _dbAverage.value) {
                        _dbAverage.value = amplitudes.average().toInt()

                        _previousDbAverage = _dbAverage.value
                    }
                    dbAverageDownloadCountDown = null
                }

                override fun onTick(time: Long) {}

            }.start()
    }

    fun stopRecording() {
        if (isRecording) {
            isRecording = false
            audioRecord?.stop()
            audioRecord?.release()
            audioRecord = null
        }
    }
}