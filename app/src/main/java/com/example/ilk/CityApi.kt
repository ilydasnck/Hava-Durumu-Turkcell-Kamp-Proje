package com.example.ilk

import com.google.gson.annotations.SerializedName
import retrofit2.http.GET
import retrofit2.http.Query

interface CityApi {
    @GET("v1/search")
    suspend fun searchCities(
        @Query("name") name: String,
        @Query("count") count: Int = 100,
        @Query("language") language: String = "tr",
        @Query("format") format: String = "json"
    ): GeocodingResponse
}

data class GeocodingResponse(
    @SerializedName("results")
    val results: List<GeocodingCityDto>?
)

data class GeocodingCityDto(
    @SerializedName("id")
    val id: Long,
    @SerializedName("name")
    val name: String,
    @SerializedName("country")
    val country: String?,
    @SerializedName("country_code")
    val countryCode: String?,
    @SerializedName("latitude")
    val latitude: Double,
    @SerializedName("longitude")
    val longitude: Double
)
