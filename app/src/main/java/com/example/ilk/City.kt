package com.example.ilk

import com.google.gson.annotations.SerializedName

/**
 * Tek bir şehre ait bilgileri temsil eden veri sınıfı (Data Class).
 */
data class City(
    @SerializedName("id")
    val id: Int,

    @SerializedName("name")
    val name: String,

    @SerializedName("country")
    val country: String,

    @SerializedName("temperature")
    val temperature: Double,

    @SerializedName("condition")
    val condition: String
)
