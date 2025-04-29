package com.example.compagnonsimu.model

data class Flight(
    val CallSign: String,
    val AircraftType: String,
    val Dep: String,
    val Arr: String,
    val Tobt: String?,
    val Ttot: String?
)