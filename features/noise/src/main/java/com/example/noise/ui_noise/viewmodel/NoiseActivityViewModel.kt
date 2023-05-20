package com.example.noise.ui_noise.viewmodel

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.media.AudioFormat
import android.media.AudioRecord
import android.os.CountDownTimer
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.noise.ui_noise.NoiseState
import com.example.noise.ui_noise.model.FrequencyState
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.OnTokenCanceledListener
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.apache.commons.math3.transform.DftNormalization
import org.apache.commons.math3.transform.FastFourierTransformer
import org.apache.commons.math3.transform.TransformType
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.log10
import kotlin.math.pow
import kotlin.math.sqrt

const val SAMPLE_RATE = 48000
const val FFT_SIZE = 512
const val BUFFER_SIZE = FFT_SIZE / 2

class NoiseActivityViewModel : ViewModel() {

    companion object {
        const val MAX_TIME_TO_WAIT_FOR_ANALYSES = 1
        const val MAX_TIME_TO_WAIT_FOR_AVERAGE_CALCULATION = 5 // Value of 5minutes
    }

    private val database = Firebase.firestore

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    private var isRecording = false
    var audioRecord: AudioRecord? = null
    private val recordingScope = CoroutineScope(Dispatchers.IO)
    private val minBufferSize: Int by lazy {
        AudioRecord.getMinBufferSize(
            SAMPLE_RATE, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT
        )
    }
    private val buffer = ShortArray(minBufferSize)
    private val fftTransformer = FastFourierTransformer(DftNormalization.UNITARY)
    private var fftBuffer = ShortArray(BUFFER_SIZE)

    val frequenciesState = MutableStateFlow(FrequencyState(doubleArrayOf(0.0)))

    val audioDecibel = MutableStateFlow(0.0)

    var noiseState = MutableStateFlow(NoiseState.LOW)

    private val amplitudes = DoubleArray(FFT_SIZE / 2)
    private val decibelValue = mutableListOf<Double>() // Array to store the DB values

    private var dbAverageCountDown: CountDownTimer? = null
    private var saveAverageDbCountDown: CountDownTimer? = null
    private val countDownInterval = 1000L

    fun initLocation(context: Context) {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
    }

    private fun sendAverageDbToFirebase(averageDb: Double, lastLocation: Location?) {
        val userRef = database.collection("data").document("average_db")
        val currentDate = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(Date())

        lastLocation?.let {
            userRef.update(
                mapOf(
                    "lat: ${lastLocation.latitude} long: ${lastLocation.longitude}" to averageDb
                )
            )
        }
    }

    @SuppressLint("MissingPermission")
    fun sendLastEmergencyLocationToFirebase() {
        val userRef = database.collection("data").document("emergency_location")

        val currentDate = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(Date())

        fusedLocationProviderClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY,
            object : CancellationToken() {
                override fun onCanceledRequested(p0: OnTokenCanceledListener): CancellationToken =
                    CancellationTokenSource().token

                override fun isCancellationRequested(): Boolean = false

            }).addOnSuccessListener { location ->
            userRef.update(mapOf(currentDate.toString() to "lat: ${location.latitude}, long: ${location.longitude}"))
        }
    }

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
            fftBuffer.map { it.toDouble() }.toDoubleArray(), TransformType.FORWARD
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
            dbAverageCountDown = object : CountDownTimer(
                countDownInterval * MAX_TIME_TO_WAIT_FOR_ANALYSES, countDownInterval
            ) {
                override fun onFinish() {
                    if (saveAverageDbCountDown == null) {
                        startDbAverageTimer()
                    }

                    audioDecibel.value = convertMagnitudeToDb()

                    if (audioDecibel.value <= 44) {
                        updateNoiseState(state = NoiseState.LOW)
                    } else if (audioDecibel.value < 70) {
                        updateNoiseState(state = NoiseState.MEDIUM)
                    } else {
                        updateNoiseState(state = NoiseState.HIGH)
                    }
                    decibelValue.add(audioDecibel.value) // add the DB values to the array
                    dbAverageCountDown = null
                }

                override fun onTick(time: Long) {}

            }.start()
        }
    }

    @SuppressLint("MissingPermission")
    private fun startDbAverageTimer() {
        saveAverageDbCountDown = object : CountDownTimer(
            countDownInterval * MAX_TIME_TO_WAIT_FOR_AVERAGE_CALCULATION, countDownInterval
        ) {
            override fun onFinish() {
                val averageDb =
                    decibelValue.average() // calculate the average decibels every 5 minutes

                fusedLocationProviderClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY,
                    object : CancellationToken() {
                        override fun onCanceledRequested(p0: OnTokenCanceledListener): CancellationToken =
                            CancellationTokenSource().token

                        override fun isCancellationRequested(): Boolean = false

                    }).addOnSuccessListener { location ->
                    sendAverageDbToFirebase(averageDb = averageDb, lastLocation = location)
                }

                decibelValue.clear() //clears the list to start the timer again
                saveAverageDbCountDown = null
            }

            override fun onTick(time: Long) {}

        }.start()
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
