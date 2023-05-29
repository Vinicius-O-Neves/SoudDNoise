package br.puc.soundmap

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import br.puc.soundmap.databinding.ActivityMainBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.TileOverlayOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.maps.android.heatmaps.HeatmapTileProvider
import com.google.maps.android.heatmaps.WeightedLatLng
import java.util.Timer
import java.util.TimerTask

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val database = Firebase.firestore

    private var mGoogleMap: GoogleMap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val timer = Timer()

        val task = object : TimerTask() {
            override fun run() {
                getDataFromFirebase()
            }
        }

        timer.schedule(task, 0, 2000)

        // Obtenha uma referência ao fragmento do mapa
        val mapFragment =
            supportFragmentManager.findFragmentById(R.id.map_fragment) as SupportMapFragment

        // Solicite o carregamento assíncrono do mapa
        mapFragment.getMapAsync { googleMap ->
            onMapReady(googleMap)
        }
    }

    private fun onMapReady(googleMap: GoogleMap) {
        mGoogleMap = googleMap

        val markerPosition = LatLng(37.4219983, -122.084)
        mGoogleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(markerPosition, 12f))
    }

    private fun getDataFromFirebase() {
        val collectionPath = "/data/average_data/locations"

        val collectionRef = database.collection(collectionPath)

        val heatMapData = mutableListOf<WeightedLatLng>()

        collectionRef.get().addOnSuccessListener { querySnapshot ->
            for (documentSnapshot in querySnapshot) {
                val data = documentSnapshot.data

                val latitude = data["latitude"].toString().toDouble()
                val longitude = data["longitude"].toString().toDouble()
                val averageDb = data["average"].toString().toDouble()

                val heatMapPoint = WeightedLatLng(LatLng(latitude, longitude), averageDb)
                heatMapData.add(heatMapPoint)

                val heatmapTileProvider = HeatmapTileProvider.Builder()
                    .weightedData(heatMapData)
                    .build()

                mGoogleMap?.addTileOverlay(TileOverlayOptions().tileProvider(heatmapTileProvider))
            }
        }.addOnFailureListener { exception ->
            Log.e("batata", "Error getting documents: $exception")
        }
    }

}

