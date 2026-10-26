package com.example.ilk.model

import com.google.gson.annotations.SerializedName

data class ForecastResponse(
    @SerializedName("latitude") val latitude: Double?,
    @SerializedName("longitude") val longitude: Double?,
    @SerializedName("current_weather") val currentWeather: CurrentWeather?,
    @SerializedName("hourly") val hourly: HourlyForecast?,
    @SerializedName("daily") val daily: DailyForecast?
)

data class CurrentWeather(
    @SerializedName("temperature") val temperature: Double?,
    @SerializedName("windspeed") val windspeed: Double?,
    @SerializedName("winddirection") val winddirection: Double?,
    @SerializedName("weathercode", alternate = ["weather_code"]) val weathercode: Int?,
    @SerializedName("is_day") val isDay: Int?
)

data class HourlyForecast(
    @SerializedName("time") val time: List<String>?,
    @SerializedName("temperature_2m") val temperature2m: List<Double>?,
    @SerializedName("relative_humidity_2m") val relativeHumidity2m: List<Int>?,
    @SerializedName("weathercode", alternate = ["weather_code"]) val weathercode: List<Int>?
)

data class DailyForecast(
    @SerializedName("time") val time: List<String>?,
    @SerializedName("weathercode", alternate = ["weather_code"]) val weathercode: List<Int>?,
    @SerializedName("temperature_2m_max") val tempMax: List<Double>?,
    @SerializedName("temperature_2m_min") val tempMin: List<Double>?
)
