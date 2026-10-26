package com.example.ilk.ui.component

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

fun getWeatherGradient(weatherCode: Int, isDay: Boolean = true, isDarkTheme: Boolean = true): Brush {
    if (!isDarkTheme) {
        return when (weatherCode) {
            0 -> Brush.verticalGradient(
                colors = listOf(
                    Color(0xFF38BDF8),
                    Color(0xFF7DD3FC),
                    Color(0xFFF0F9FF)
                )
            )
            in 51..82 -> Brush.verticalGradient(
                colors = listOf(
                    Color(0xFF64748B),
                    Color(0xFF94A3B8),
                    Color(0xFFF1F5F9)
                )
            )
            else -> Brush.verticalGradient(
                colors = listOf(
                    Color(0xFF7DD3FC),
                    Color(0xFFBAE6FD),
                    Color(0xFFF8FAFC)
                )
            )
        }
    }

    return when (weatherCode) {
        0 -> if (isDay) {
            Brush.verticalGradient(colors = listOf(Color(0xFFD97706), Color(0xFFB45309), Color(0xFF0F172A)))
        } else {
            Brush.verticalGradient(colors = listOf(Color(0xFF0A1128), Color(0xFF1C2541), Color(0xFF000814)))
        }
        1, 2 -> if (isDay) {
            Brush.verticalGradient(colors = listOf(Color(0xFF0284C7), Color(0xFF0369A1), Color(0xFF0C4A6E)))
        } else {
            Brush.verticalGradient(colors = listOf(Color(0xFF0A1128), Color(0xFF1C2541), Color(0xFF000814)))
        }
        3, 45, 48 -> Brush.verticalGradient(colors = listOf(Color(0xFF334155), Color(0xFF1E293B), Color(0xFF0F172A)))
        in 51..82 -> Brush.verticalGradient(colors = listOf(Color(0xFF1E1B4B), Color(0xFF312E81), Color(0xFF0F172A)))
        in 71..86 -> Brush.verticalGradient(colors = listOf(Color(0xFF0891B2), Color(0xFF0E7490), Color(0xFF164E63)))
        in 95..99 -> Brush.verticalGradient(colors = listOf(Color(0xFF581C87), Color(0xFF3B0764), Color(0xFF0F172A)))
        else -> Brush.verticalGradient(colors = listOf(Color(0xFF0F172A), Color(0xFF1E293B), Color(0xFF020617)))
    }
}

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
