package com.example.ilk.model

data class City(
    val cityName: String,
    val latitude: Double,
    val longitude: Double,
    val temperature: String = "...",
    val condition: String = "Yükleniyor...",
    val isLoading: Boolean = true
)

data class CityCoordinates(
    val name: String,
    val country: String,
    val latitude: Double,
    val longitude: Double
)
