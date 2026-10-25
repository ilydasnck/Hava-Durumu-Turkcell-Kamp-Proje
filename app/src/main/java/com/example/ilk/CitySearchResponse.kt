package com.example.ilk

import com.google.gson.annotations.SerializedName

/**
 * API'den (Sunucudan) gelen arama veya listeleme yanıtının en dıştaki (kök) gövdesini temsil eder.
 */
data class CitySearchResponse(
    @SerializedName("count")
    val count: Int,

    @SerializedName("cities")
    val cities: List<City>
)
