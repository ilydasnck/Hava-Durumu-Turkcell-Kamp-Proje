package com.example.ilk

data class CityWeather(
    val cityName: String,
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val temperature: String = "...",
    val condition: String = "Yükleniyor...",
    val isLoading: Boolean = true
)
