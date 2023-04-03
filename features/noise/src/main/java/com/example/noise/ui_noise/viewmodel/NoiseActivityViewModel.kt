package com.example.noise.ui_noise.viewmodel

import android.media.AudioFormat
import android.media.AudioRecord
import android.os.CountDownTimer
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.noise.ui_noise.NoiseState
import com.example.noise.ui_noise.model.FrequencyState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.apache.commons.math3.transform.DftNormalization
import org.apache.commons.math3.transform.FastFourierTransformer
import org.apache.commons.math3.transform.TransformType
import kotlin.math.log10
import kotlin.math.pow
import kotlin.math.sqrt

const val SAMPLE_RATE = 48000
const val FFT_SIZE = 512
const val BUFFER_SIZE = FFT_SIZE / 2

class NoiseActivityViewModel : ViewModel() {

    companion object {
        const val MAX_TIME_TO_WAIT_FOR_ANALYSES = 1
    }

    private var isRecording = false
    var audioRecord: AudioRecord? = null
    private val recordingScope = CoroutineScope(Dispatchers.IO)
    private val minBufferSize: Int by lazy {
        AudioRecord.getMinBufferSize(
            SAMPLE_RATE,
            AudioFormat.CHANNEL_IN_MONO,
            AudioFormat.ENCODING_PCM_16BIT
        )
    }
    private val buffer = ShortArray(minBufferSize)
    private val fftTransformer = FastFourierTransformer(DftNormalization.UNITARY)
    private var fftBuffer = ShortArray(BUFFER_SIZE)

    val frequenciesState = MutableStateFlow(FrequencyState(doubleArrayOf(0.0)))

    val audioDecibel = MutableStateFlow(0.0)

    var noiseState = MutableStateFlow(NoiseState.LOW)

    private val amplitudes = DoubleArray(FFT_SIZE / 2)

    private var dbAverageCountDown: CountDownTimer? = null
    private val countDownInterval = 1000L

    fun startRecording() {
        recordingScope.launch(Dispatchers.IO) {
            if (isRecording.not()) {
                isRecording = true
                audioRecord?.startRecording()

                while (isRecording) {
                    getMagnitudesFromRecordingAudio().catch {
                        Log.d("frequencies", "error")
                    }.collect { magnitudes ->
                        setAudioAmplitudes(magnitudes = magnitudes)
                    }
                }
            }
        }
    }

    private fun getMagnitudesFromRecordingAudio(): Flow<DoubleArray> = flow {
        withContext(Dispatchers.IO) {
            audioRecord?.read(buffer, 0, minBufferSize)
        }

        fftBuffer = buffer.copyOf(BUFFER_SIZE)
        val fftResult = fftTransformer.transform(
            fftBuffer.map { it.toDouble() }.toDoubleArray(),
            TransformType.FORWARD
        )

        val magnitudes = DoubleArray(FFT_SIZE / 2)
        for (i in 0 until FFT_SIZE / 2) {
            val real = fftResult[i].real
            val image = fftResult[i].imaginary
            magnitudes[i] = sqrt(real * real + image * image)
        }

        emit(magnitudes)
    }.flowOn(Dispatchers.Default)

    private fun setAudioAmplitudes(magnitudes: DoubleArray) {
        recordingScope.launch(Dispatchers.Default) {
            for (i in amplitudes.indices) {
                val magnitude = magnitudes[i]

                if (magnitude > 0.0) {
                    val db = 20 * log10(magnitude)
                    amplitudes[i] = 10.0.pow(db / 20.0)
                }
            }

            frequenciesState.value = frequenciesState.value.copy(
                frequencies = amplitudes.sliceArray(6 until 12),
            )

            if (dbAverageCountDown == null) {
                startDbAverageDownloadCountDown()
            }
        }
    }

    private fun startDbAverageDownloadCountDown() {
        viewModelScope.launch {
            dbAverageCountDown =
                object : CountDownTimer(
                    countDownInterval * MAX_TIME_TO_WAIT_FOR_ANALYSES,
                    countDownInterval
                ) {
                    override fun onFinish() {
                        audioDecibel.value = convertMagnitudeToDb()

                        if (audioDecibel.value <= 30) {
                            updateNoiseState(state = NoiseState.LOW)
                        } else if (audioDecibel.value < 55) {
                            updateNoiseState(state = NoiseState.MEDIUM)
                        } else {
                            updateNoiseState(state = NoiseState.HIGH)
                        }

                        dbAverageCountDown = null
                    }

                    override fun onTick(time: Long) {}

                }.start()
        }
    }

    private fun convertMagnitudeToDb(): Double {
        val rms = sqrt(amplitudes.map { it * it }.average())

        return 20.0 * log10(rms)
    }

    private fun updateNoiseState(state: NoiseState) {
        noiseState.value = state
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