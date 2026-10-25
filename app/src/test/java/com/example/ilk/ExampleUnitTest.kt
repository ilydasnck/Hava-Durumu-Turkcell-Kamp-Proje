package com.example.ilk

import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.Assert.*

class ExampleUnitTest {
    @Test
    fun testWeatherApi() = runBlocking {
        val response = ApiClient.forecastApi.getWeather(41.0, 28.97)
        assertNotNull(response.currentWeather)

        val detail = ApiClient.forecastApi.getDetailWeather(41.0, 28.97)
        assertNotNull(detail.currentWeather)
        assertNotNull(detail.hourly)
        assertNotNull(detail.daily)
    }
}
