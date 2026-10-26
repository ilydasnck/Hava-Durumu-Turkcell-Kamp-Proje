package com.example.ilk.ui.city

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ilk.data.CityRepository
import com.example.ilk.data.WeatherRepository
import com.example.ilk.model.City
import com.example.ilk.ui.LoadState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CityViewModel(
    private val cityRepository: CityRepository = CityRepository(),
    private val weatherRepository: WeatherRepository = WeatherRepository()
) : ViewModel() {

    private val _citiesState = MutableStateFlow<LoadState<List<City>>>(LoadState.Loading)
    val citiesState: StateFlow<LoadState<List<City>>> = _citiesState.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    init {
        loadCities()
    }

    fun loadCities() {
        viewModelScope.launch {
            _citiesState.value = LoadState.Loading
            try {
                val list = cityRepository.getTurkishCities()
                if (list.isEmpty()) {
                    _citiesState.value = LoadState.Empty
                } else {
                    _citiesState.value = LoadState.Success(list)
                }
            } catch (e: Exception) {
                _citiesState.value = LoadState.Error(e.message ?: "Şehirler yüklenemedi")
            }
        }
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    suspend fun fetchWeatherForCity(city: City): Pair<String, String> {
        return weatherRepository.fetchWeatherForCity(city.latitude, city.longitude)
    }
}
