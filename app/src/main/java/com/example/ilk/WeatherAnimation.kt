package com.example.ilk

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.unit.dp

@Composable
fun WeatherAnimation(
    weatherCode: Int,
    isDay: Boolean = true,
    modifier: Modifier = Modifier.size(140.dp)
) {
    val infiniteTransition = rememberInfiniteTransition(label = "weatherAnimation")

    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(12000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )

    val offsetProgress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "offsetProgress"
    )

    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height
        val center = Offset(width / 2f, height / 2f)

        when (weatherCode) {
            // Güneşli / Açık
            0 -> {
                val sunColor = if (isDay) Color(0xFFFFB300) else Color(0xFFE0E0E0)
                val rayColor = if (isDay) Color(0xFFFFD54F) else Color(0xFFBDBDBD)

                rotate(rotation, pivot = center) {
                    for (i in 0..7) {
                        val angle = Math.toRadians((i * 45).toDouble())
                        val startX = center.x + (width * 0.28f * Math.cos(angle)).toFloat()
                        val startY = center.y + (height * 0.28f * Math.sin(angle)).toFloat()
                        val endX = center.x + (width * 0.42f * Math.cos(angle)).toFloat()
                        val endY = center.y + (height * 0.42f * Math.sin(angle)).toFloat()

                        drawLine(
                            color = rayColor,
                            start = Offset(startX, startY),
                            end = Offset(endX, endY),
                            strokeWidth = 6.dp.toPx()
                        )
                    }
                }
                drawCircle(color = sunColor, radius = width * 0.22f, center = center)
            }

            // Yağmurlu
            in 51..82 -> {
                val cloudColor = Color(0xFF78909C)
                val dropColor = Color(0xFF29B6F6)

                // Bulutlar
                drawCircle(color = cloudColor, radius = width * 0.22f, center = Offset(center.x - width * 0.12f, center.y - height * 0.1f))
                drawCircle(color = cloudColor, radius = width * 0.26f, center = Offset(center.x + width * 0.08f, center.y - height * 0.12f))

                // Düşen Yağmur Damlaları
                for (i in 0..5) {
                    val x = width * (0.25f + i * 0.1f)
                    val y = (center.y + (offsetProgress * height * 0.35f) + (i * 12)) % (height * 0.9f)
                    if (y > center.y) {
                        drawLine(
                            color = dropColor,
                            start = Offset(x, y),
                            end = Offset(x - 4f, y + 16.dp.toPx()),
                            strokeWidth = 3.dp.toPx()
                        )
                    }
                }
            }

            // Karlı
            in 71..86 -> {
                val cloudColor = Color(0xFF90A4AE)
                val snowColor = Color.White

                drawCircle(color = cloudColor, radius = width * 0.22f, center = Offset(center.x - width * 0.12f, center.y - height * 0.1f))
                drawCircle(color = cloudColor, radius = width * 0.26f, center = Offset(center.x + width * 0.08f, center.y - height * 0.12f))

                for (i in 0..5) {
                    val x = width * (0.22f + i * 0.12f) + (Math.sin(offsetProgress * 6.28 + i).toFloat() * 10f)
                    val y = (center.y + (offsetProgress * height * 0.35f) + (i * 15)) % (height * 0.9f)
                    if (y > center.y) {
                        drawCircle(color = snowColor, radius = 5.dp.toPx(), center = Offset(x, y))
                    }
                }
            }

            // Bulutlu / Sisli / Diğer
            else -> {
                val cloudColor1 = Color(0xFFB0BEC5)
                val cloudColor2 = Color(0xFF78909C)

                drawCircle(color = cloudColor1, radius = width * 0.22f, center = Offset(center.x - width * 0.14f + (offsetProgress * 10f), center.y - height * 0.05f))
                drawCircle(color = cloudColor2, radius = width * 0.26f, center = Offset(center.x + width * 0.08f - (offsetProgress * 10f), center.y - height * 0.08f))
            }
        }
    }
}

// Open-Meteo hava durum kodlarını Türkçe metne dönüştürür
fun parseWeatherCodeToText(code: Int): String {
    return when (code) {
        0 -> "Açık / Güneşli"
        1, 2, 3 -> "Parçalı Bulutlu"
        45, 48 -> "Sisli"
        51, 53, 55 -> "Hafif Çiseleyen Yağmur"
        61, 63, 65 -> "Yağmurlu"
        71, 73, 75, 77 -> "Karlı"
        80, 81, 82 -> "Sağanak Yağışlı"
        85, 86 -> "Yoğun Kar Yağışlı"
        95, 96, 99 -> "Gökgürültülü Fırtına"
        else -> "Parçalı Bulutlu"
    }
}
