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
 * Weather Codes & Colors (Open-Meteo WMO Codes):
 * - 0: Clear Sky / Güneşli -> Sıcak Altın Kehribar (Amber) & Turuncu
 * - 1, 2: Az / Parçalı Bulutlu -> Berrak Gökyüzü Mavisı
 * - 3, 45, 48: Bulutlu / Sisli -> Koyu Dumanlı Mavi-Gri
 * - 51..82: Yağmurlu / Sağanak -> Sakinleştirici Derin Lacivert Yağmur Tonu
 * - 71..86: Karlı -> Soğuk Buz Mavisı
 * - 95..99: Fırtınalı -> Elektrik Mor Fırtına
 */
fun getWeatherGradient(weatherCode: Int, isDay: Boolean = true): Brush {
    return when (weatherCode) {
        // 0: Tam Güneşli / Açık (Sıcak Altın Kehribar & Turuncu)
        0 -> if (isDay) {
            Brush.verticalGradient(
                colors = listOf(
                    Color(0xFFD97706), // Sıcak Kehribar Altın
                    Color(0xFFB45309), // Turuncu Güneş
                    Color(0xFF0F172A)  // Derin Taban
                )
            )
        } else {
            Brush.verticalGradient(
                colors = listOf(
                    Color(0xFF0A1128),
                    Color(0xFF1C2541),
                    Color(0xFF000814)
                )
            )
        }

        // 1, 2: Az / Parçalı Bulutlu (Berrak Gökyüzü Mavisi)
        1, 2 -> if (isDay) {
            Brush.verticalGradient(
                colors = listOf(
                    Color(0xFF0284C7), // Berrak Gökyüzü Mavisi
                    Color(0xFF0369A1), // Mavi
                    Color(0xFF0C4A6E)  // Koyu Mavi
                )
            )
        } else {
            Brush.verticalGradient(
                colors = listOf(
                    Color(0xFF0A1128),
                    Color(0xFF1C2541),
                    Color(0xFF000814)
                )
            )
        }

        // 3, 45, 48: Bulutlu / Sisli (Koyu Füme & Kömür Grisi)
        3, 45, 48 -> Brush.verticalGradient(
            colors = listOf(
                Color(0xFF334155), // Füme Gri
                Color(0xFF1E293B), // Koyu Slate
                Color(0xFF0F172A)  // Taban
            )
        )

        // 51..82: Yağmurlu / Sağanak / Çiseleyen (Derin Lacivert Yağmur)
        in 51..82 -> Brush.verticalGradient(
            colors = listOf(
                Color(0xFF1E1B4B), // Derin Gece Yağmuru
                Color(0xFF312E81), // Fırtına Laciverti
                Color(0xFF0F172A)  // Koyu Taban
            )
        )

        // 71..86: Karlı (Soğuk Buz Siyan Mavisi)
        in 71..86 -> Brush.verticalGradient(
            colors = listOf(
                Color(0xFF0891B2), // Buz Mavisı
                Color(0xFF0E7490), // Glasiyer Tonu
                Color(0xFF164E63)  // Koyu Kış Laciverti
            )
        )

        // 95..99: Fırtınalı (Elektrik Fırtına Moru)
        in 95..99 -> Brush.verticalGradient(
            colors = listOf(
                Color(0xFF581C87), // Elektrik Mor
                Color(0xFF3B0764), // Derin Mor
                Color(0xFF0F172A)  // Siyah
            )
        )

        // Diğer Durumlar
        else -> Brush.verticalGradient(
            colors = listOf(
                Color(0xFF0F172A),
                Color(0xFF1E293B),
                Color(0xFF020617)
            )
        )
    }
}

// Cam Biçimi (Glassmorphism) Efekti Modifier'ı
fun Modifier.glassmorphism(
    cornerRadius: Dp = 24.dp,
    backgroundColor: Color = Color.White.copy(alpha = 0.08f),
    borderColor: Color = Color.White.copy(alpha = 0.15f),
    borderWidth: Dp = 1.dp
): Modifier = this
    .clip(RoundedCornerShape(cornerRadius))
    .background(backgroundColor)
    .border(
        border = BorderStroke(borderWidth, borderColor),
        shape = RoundedCornerShape(cornerRadius)
    )
