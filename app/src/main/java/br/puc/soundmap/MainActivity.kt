package br.puc.soundmap

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import android.view.MenuItem
import br.puc.soundmap.databinding.ActivityMainBinding
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.maps.android.heatmaps.HeatmapTileProvider
import com.google.android.gms.maps.model.TileOverlayOptions
import com.google.android.gms.maps.model.LatLng




class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        binding.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        // Obtenha uma referência ao fragmento do mapa
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map_fragment) as SupportMapFragment

        // Solicite o carregamento assíncrono do mapa
        mapFragment.getMapAsync { googleMap ->
            onMapReady(googleMap)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

    fun onMapReady(googleMap: GoogleMap) {
        var mMap = googleMap

        // Crie uma lista de LatLng com os pontos do mapa de calor
        val heatmapData = mutableListOf<LatLng>()
        heatmapData.add(LatLng(-33.87365, 151.20689)) // Exemplo de coordenada
        // Adicione mais pontos conforme necessário

        // Crie o provedor de mapa de calor
        val heatmapTileProvider = HeatmapTileProvider.Builder()
            .data(heatmapData)
            .build()

        // Adicione a camada do mapa de calor ao mapa
        val heatmapOverlay = mMap.addTileOverlay(TileOverlayOptions().tileProvider(heatmapTileProvider))
    }

}

