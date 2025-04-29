package com.example.compagnonsimu.model


data class ApiResponse(
    val NewDepartureFlights: List<Flight>,
    val NewArrivalFlights: List<Flight>,
    val NewTaxibots: List<Taxibot>,
    val NewDrones: List<Any>,
    val NewHelicopters: List<Any>
)