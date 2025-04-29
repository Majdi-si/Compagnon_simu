package com.example.compagnonsimu

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.example.compagnonsimu.adapter.FlightAdapter
import com.example.compagnonsimu.model.ApiResponse

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerView)

        val json = loadJsonFromAssets()
        val data = Gson().fromJson(json, ApiResponse::class.java)

        val items = mutableListOf<Any>().apply {
            addAll(data.NewDepartureFlights)
            addAll(data.NewArrivalFlights)
            addAll(data.NewTaxibots)
        }

        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = FlightAdapter(items)
            addItemDecoration(
                DividerItemDecoration(
                    this@MainActivity,
                    DividerItemDecoration.VERTICAL
                )
            )
        }
    }

    private fun loadJsonFromAssets(): String {
        return assets.open("CDG_01_09_2019_8h40_10h15_HEAVY.json")
            .bufferedReader()
            .use { it.readText() }
    }
}