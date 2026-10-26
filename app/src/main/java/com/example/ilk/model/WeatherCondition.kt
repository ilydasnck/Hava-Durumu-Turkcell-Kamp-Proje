package com.example.ilk.model

fun parseWeatherCodeToText(code: Int): String {
    return when (code) {
        0 -> "Açık / Güneşli"
        1, 2 -> "Parçalı Bulutlu"
        3 -> "Çok Bulutlu"
        45, 48 -> "Sisli"
        51, 53, 55 -> "Hafif Çiseleme"
        61, 63, 65 -> "Yağmurlu"
        71, 73, 75 -> "Kar Yağışlı"
        80, 81, 82 -> "Sağanak Yağışlı"
        95, 96, 99 -> "Gökgürültülü Fırtına"
        else -> "Bilinmiyor"
    }
}

fun parseWeatherCodeToEmoji(code: Int): String {
    return when (code) {
        0 -> "☀️"
        1, 2 -> "🌤️"
        3, 45, 48 -> "☁️"
        in 51..82 -> "🌧️"
        in 71..86 -> "❄️"
        in 95..99 -> "⚡"
        else -> "⛅"
    }
}
