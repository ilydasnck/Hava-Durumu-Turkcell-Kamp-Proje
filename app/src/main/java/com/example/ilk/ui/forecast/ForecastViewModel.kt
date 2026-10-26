package com.example.ilk.ui.forecast

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ilk.data.WeatherRepository
import com.example.ilk.model.ForecastResponse
import com.example.ilk.ui.LoadState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ForecastViewModel(
    private val weatherRepository: WeatherRepository = WeatherRepository()
) : ViewModel() {

    private val _forecastState = MutableStateFlow<LoadState<ForecastResponse>>(LoadState.Loading)
    val forecastState: StateFlow<LoadState<ForecastResponse>> = _forecastState.asStateFlow()

    fun loadForecast(lat: Double, lon: Double) {
        viewModelScope.launch {
            _forecastState.value = LoadState.Loading
            val response = weatherRepository.fetchDetailedWeather(lat, lon)
            if (response != null) {
                _forecastState.value = LoadState.Success(response)
            } else {
                _forecastState.value = LoadState.Error("Hava durumu verisi çekilemedi")
            }
        }
    }
}
