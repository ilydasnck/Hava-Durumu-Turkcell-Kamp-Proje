package com.example.ilk

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Modern hava durumu gradientleri — iOS Weather tarzı, her durum için belirgin renk.
 * isDay artık her zaman true olarak gelecek (gece modu kapalı).
 */
fun getWeatherGradient(weatherCode: Int, isDay: Boolean = true, isDarkTheme: Boolean = true): Brush {
    return if (isDarkTheme) {
        getDarkGradient(weatherCode)
    } else {
        getLightGradient(weatherCode)
    }
}

private fun getDarkGradient(weatherCode: Int): Brush {
    return when (weatherCode) {
        // Güneşli / Açık — Modern derin gökyüzü mavisi (iOS Weather stili)
        0 -> Brush.verticalGradient(
            colors = listOf(
                Color(0xFF0A2744), // Çok derin gece mavisi üst
                Color(0xFF1A5276), // Derin okyanus mavisi
                Color(0xFF2980B9)  // Canlı gök mavisi
            )
        )

        // Hafif Bulutlu — Biraz daha açık mavi
        1 -> Brush.verticalGradient(
            colors = listOf(
                Color(0xFF0D3B6E),
                Color(0xFF1F618D),
                Color(0xFF2E86C1)
            )
        )

        // Parçalı Bulutlu — Orta koyu mavi-gri karışımı
        2 -> Brush.verticalGradient(
            colors = listOf(
                Color(0xFF17202A),
                Color(0xFF1F3044),
                Color(0xFF2C4770)
            )
        )

        // Çok Bulutlu / Kapalı — Belirgin kurşuni gri
        3 -> Brush.verticalGradient(
            colors = listOf(
                Color(0xFF1C2833),
                Color(0xFF2E4057),
                Color(0xFF424F5C)
            )
        )

        // Sisli — Koyu teal-gri
        45, 48 -> Brush.verticalGradient(
            colors = listOf(
                Color(0xFF0E3840),
                Color(0xFF1A5C5C),
                Color(0xFF117A65)
            )
        )

        // Çisenti / Hafif Yağmur
        in 51..55 -> Brush.verticalGradient(
            colors = listOf(
                Color(0xFF1A237E),
                Color(0xFF283593),
                Color(0xFF3949AB)
            )
        )

        // Dondurucu Çisenti
        in 56..57 -> Brush.verticalGradient(
            colors = listOf(
                Color(0xFF0D2137),
                Color(0xFF0C3547),
                Color(0xFF0A6070)
            )
        )

        // Yağmur — Koyu kobalt
        in 61..67 -> Brush.verticalGradient(
            colors = listOf(
                Color(0xFF0D1B3E),
                Color(0xFF1A2F6E),
                Color(0xFF1B4FAF)
            )
        )

        // Sağanak Yağmur — Daha koyu
        in 80..82 -> Brush.verticalGradient(
            colors = listOf(
                Color(0xFF060D1E),
                Color(0xFF0D1B3E),
                Color(0xFF1A2F6E)
            )
        )

        // Kar — Buzul mavisi
        in 71..77 -> Brush.verticalGradient(
            colors = listOf(
                Color(0xFF0B2545),
                Color(0xFF0A4C6A),
                Color(0xFF0D7F9F)
            )
        )

        // Kar Sağanağı
        in 85..86 -> Brush.verticalGradient(
            colors = listOf(
                Color(0xFF0A2535),
                Color(0xFF0E6E8A),
                Color(0xFF48CAE4)
            )
        )

        // Fırtına / Şimşek — Derin mor
        in 95..99 -> Brush.verticalGradient(
            colors = listOf(
                Color(0xFF0D0221),
                Color(0xFF3B0764),
                Color(0xFF6B21A8)
            )
        )

        else -> Brush.verticalGradient(
            colors = listOf(
                Color(0xFF0A2744),
                Color(0xFF1A5276),
                Color(0xFF2980B9)
            )
        )
    }
}

private fun getLightGradient(weatherCode: Int): Brush {
    return when (weatherCode) {
        // Güneşli: Gökyüzü mavisi — sarı yok
        0 -> Brush.verticalGradient(
            colors = listOf(
                Color(0xFF1565C0),
                Color(0xFF1E88E5),
                Color(0xFF64B5F6)
            )
        )
        1 -> Brush.verticalGradient(
            colors = listOf(
                Color(0xFF1976D2),
                Color(0xFF42A5F5),
                Color(0xFFBBDEFB)
            )
        )
        2 -> Brush.verticalGradient(
            colors = listOf(
                Color(0xFF455A64),
                Color(0xFF78909C),
                Color(0xFFB0BEC5)
            )
        )
        3 -> Brush.verticalGradient(
            colors = listOf(
                Color(0xFF37474F),
                Color(0xFF607D8B),
                Color(0xFFCFD8DC)
            )
        )
        45, 48 -> Brush.verticalGradient(
            colors = listOf(
                Color(0xFF00695C),
                Color(0xFF26A69A),
                Color(0xFFB2DFDB)
            )
        )
        in 51..67, in 80..82 -> Brush.verticalGradient(
            colors = listOf(
                Color(0xFF1565C0),
                Color(0xFF42A5F5),
                Color(0xFFBBDEFB)
            )
        )
        in 71..77, in 85..86 -> Brush.verticalGradient(
            colors = listOf(
                Color(0xFF0277BD),
                Color(0xFF29B6F6),
                Color(0xFFE1F5FE)
            )
        )
        in 95..99 -> Brush.verticalGradient(
            colors = listOf(
                Color(0xFF4527A0),
                Color(0xFF7E57C2),
                Color(0xFFEDE7F6)
            )
        )
        else -> Brush.verticalGradient(
            colors = listOf(
                Color(0xFF1565C0),
                Color(0xFF42A5F5),
                Color(0xFFBBDEFB)
            )
        )
    }
}

// Cam Efekti (Glassmorphism) Modifier
fun Modifier.glassmorphism(
    cornerRadius: Dp = 24.dp,
    backgroundColor: Color = Color.White.copy(alpha = 0.10f),
    borderColor: Color = Color.White.copy(alpha = 0.20f),
    borderWidth: Dp = 1.dp
): Modifier = this
    .clip(RoundedCornerShape(cornerRadius))
    .background(backgroundColor)
    .border(
        border = BorderStroke(borderWidth, borderColor),
        shape = RoundedCornerShape(cornerRadius)
    )
