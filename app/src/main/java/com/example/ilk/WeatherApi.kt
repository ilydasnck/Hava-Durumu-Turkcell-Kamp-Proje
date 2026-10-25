package com.example.ilk

import com.google.gson.annotations.SerializedName
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {
    @GET("v1/forecast")
    suspend fun getWeather(
        @Query("latitude") lat: Double,
        @Query("longitude") lon: Double,
        @Query("current_weather") currentWeather: Boolean = true
    ): WeatherApiResponse

    @GET("v1/forecast")
    suspend fun getDetailWeather(
        @Query("latitude") lat: Double,
        @Query("longitude") lon: Double,
        @Query("current_weather") currentWeather: Boolean = true,
        @Query("hourly") hourly: String = "temperature_2m,relative_humidity_2m,weather_code",
        @Query("daily") daily: String = "temperature_2m_max,temperature_2m_min,weather_code",
        @Query("timezone") timezone: String = "auto"
    ): WeatherDetailResponse
}

data class WeatherApiResponse(
    @SerializedName("current_weather")
    val currentWeather: CurrentWeatherData?
)

data class CurrentWeatherData(
    @SerializedName("temperature")
    val temperature: Double,
    @SerializedName("weathercode", alternate = ["weather_code"])
    val weathercode: Int?
)

data class WeatherDetailResponse(
    @SerializedName("latitude") val latitude: Double?,
    @SerializedName("longitude") val longitude: Double?,
    @SerializedName("current_weather") val currentWeather: CurrentWeatherDetail?,
    @SerializedName("hourly") val hourly: HourlyWeatherDetail?,
    @SerializedName("daily") val daily: DailyWeatherDetail?
)

data class CurrentWeatherDetail(
    @SerializedName("temperature") val temperature: Double?,
    @SerializedName("windspeed") val windspeed: Double?,
    @SerializedName("winddirection") val winddirection: Double?,
    @SerializedName("weathercode", alternate = ["weather_code"]) val weathercode: Int?,
    @SerializedName("is_day") val isDay: Int?
)

data class HourlyWeatherDetail(
    @SerializedName("time") val time: List<String>?,
    @SerializedName("temperature_2m") val temperature2m: List<Double>?,
    @SerializedName("relative_humidity_2m") val relativeHumidity2m: List<Double>?,
    @SerializedName("weathercode", alternate = ["weather_code"]) val weathercode: List<Int>?
)

data class DailyWeatherDetail(
    @SerializedName("time") val time: List<String>?,
    @SerializedName("weathercode", alternate = ["weather_code"]) val weathercode: List<Int>?,
    @SerializedName("temperature_2m_max") val tempMax: List<Double>?,
    @SerializedName("temperature_2m_min") val tempMin: List<Double>?
)
