package com.example.compagnonsimu.model

data class Taxibot(
    val CallSign: String,
    val AircraftType: String,
    val Parking: String,
    val Missions: List<String>
)