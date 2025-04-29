package com.example.compagnonsimu

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.example.compagnonsimu.adapter.FlightAdapter
import com.example.compagnonsimu.model.ApiResponse
import androidx.activity.result.contract.ActivityResultContracts

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var flightAdapter: FlightAdapter

    companion object {
        private const val PICK_JSON_FILE = 1001
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialisation de la Toolbar
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayShowTitleEnabled(false)
        recyclerView = findViewById(R.id.recyclerView)
        flightAdapter = FlightAdapter(emptyList())

        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = flightAdapter
            val divider = DividerItemDecoration(recyclerView.context, LinearLayoutManager.VERTICAL)
            recyclerView.addItemDecoration(divider)

        }

        // Chargement du fichier JSON par défaut au démarrage
        loadDefaultJson()
    }

    private fun loadDefaultJson() {
        val json = loadJsonFromAssets("CDG_01_09_2019_8h40_10h15_HEAVY.json")
        val data = Gson().fromJson(json, ApiResponse::class.java)

        val items = mutableListOf<Any>().apply {
            addAll(data.NewDepartureFlights)
            addAll(data.NewArrivalFlights)
            addAll(data.NewTaxibots)
        }

        flightAdapter = FlightAdapter(items)
        recyclerView.adapter = flightAdapter
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_action_import -> {  // Modifié
                launchFilePicker()
                true
            }
            R.id.menu_action_connect -> {  // Modifié
                connectToSimulator()
                true
            }
            R.id.menu_action_sort -> {
                showSortDialog()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private val filePickerLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.data?.let { uri ->
                try {
                    contentResolver.openInputStream(uri)?.use { stream ->
                        val json = stream.bufferedReader().use { it.readText() }
                        val data = Gson().fromJson(json, ApiResponse::class.java)

                        val items = mutableListOf<Any>().apply {
                            addAll(data.NewDepartureFlights)
                            addAll(data.NewArrivalFlights)
                            addAll(data.NewTaxibots)
                        }

                        flightAdapter = FlightAdapter(items)
                        recyclerView.adapter = flightAdapter
                        Toast.makeText(this, "Fichier importé avec succès", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    Toast.makeText(this, "Erreur lors de la lecture du fichier", Toast.LENGTH_SHORT).show()
                    e.printStackTrace()
                }
            }
        }
    }

    private fun launchFilePicker() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "application/json"
        }
        filePickerLauncher.launch(intent) // <-- Nouvelle méthode
    }

    private fun connectToSimulator() {
        // Placeholder pour la future connexion Ivy
        Toast.makeText(this, "Connexion au simulateur - Fonctionnalité à venir", Toast.LENGTH_SHORT).show()
    }

    @Deprecated("Deprecated in Java")

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_JSON_FILE && resultCode == Activity.RESULT_OK) {
            data?.data?.let { uri ->
                try {
                    contentResolver.openInputStream(uri)?.use { stream ->
                        val json = stream.bufferedReader().use { it.readText() }
                        val data  = Gson().fromJson(json, ApiResponse::class.java)

                        val items = mutableListOf<Any>().apply {
                            addAll(data.NewDepartureFlights)
                            addAll(data.NewArrivalFlights)
                            addAll(data.NewTaxibots)
                        }

                        flightAdapter = FlightAdapter(items)
                        recyclerView.adapter = flightAdapter
                        Toast.makeText(this, "Fichier importé avec succès", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    Toast.makeText(this, "Erreur lors de la lecture du fichier", Toast.LENGTH_SHORT).show()
                    e.printStackTrace()
                }
            }
        }
    }

    private fun loadJsonFromAssets(fileName: String): String {
        return assets.open(fileName)
            .bufferedReader()
            .use { it.readText() }
    }
    private fun showSortDialog() {
        val options = arrayOf(
            "Type de mobile (Avion/Tug)",
            "Départ vs Arrivée",
            "Début de roulage",
            "Callsign alphabétique"
        )
        androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("Trier par")
            .setItems(options) { _, which ->
                flightAdapter.sortByOption(which)
            }
            .show()
    }

}