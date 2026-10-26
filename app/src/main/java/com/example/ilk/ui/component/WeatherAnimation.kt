package com.example.ilk.ui.component

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color

@Composable
fun WeatherAnimation(
    weatherCode: Int,
    isDay: Boolean = true,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "weather")
    
    val bounceOffset by infiniteTransition.animateFloat(
        initialValue = -10f,
        targetValue = 10f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "bounce"
    )

    Canvas(modifier = modifier.fillMaxSize()) {
        val center = Offset(size.width / 2f, size.height / 2f + bounceOffset)
        val radius = size.minDimension / 3f

        when (weatherCode) {
            0 -> {
                drawCircle(
                    color = if (isDay) Color(0xFFF59E0B) else Color(0xFFE2E8F0),
                    radius = radius,
                    center = center
                )
            }
            1, 2, 3 -> {
                drawCircle(
                    color = Color.White.copy(alpha = 0.9f),
                    radius = radius * 0.7f,
                    center = Offset(center.x - radius * 0.3f, center.y)
                )
                drawCircle(
                    color = Color.White.copy(alpha = 0.9f),
                    radius = radius * 0.8f,
                    center = center
                )
                drawCircle(
                    color = Color.White.copy(alpha = 0.9f),
                    radius = radius * 0.6f,
                    center = Offset(center.x + radius * 0.4f, center.y)
                )
            }
            else -> {
                drawCircle(
                    color = Color(0xFF38BDF8),
                    radius = radius * 0.8f,
                    center = center
                )
            }
        }
    }
}
