package com.example.ilk

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.ilk.ui.theme.IlkTheme

@Composable
fun CityCard(
    cityName: String,
    temperature: String,
    condition: String,
    modifier: Modifier = Modifier,
    isSelected: Boolean = false,
    onClick: () -> Unit = {}
) {
    val isDarkTheme = MaterialTheme.colorScheme.background.luminance() < 0.5f

    val containerColor = if (isSelected) {
        if (isDarkTheme) Color(0xFF0369A1) else Color(0xFFE0F2FE)
    } else {
        if (isDarkTheme) Color(0xFF1E293B) else Color.White
    }

    val textColor = if (isDarkTheme) Color.White else Color(0xFF0F172A)
    val subTextColor = if (isDarkTheme) Color.White.copy(alpha = 0.75f) else Color(0xFF475569)
    val tempColor = if (isSelected) {
        Color.White
    } else {
        if (isDarkTheme) Color(0xFF38BDF8) else Color(0xFF0284C7)
    }

    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isSelected) 8.dp else 2.dp,
            pressedElevation = 2.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = containerColor
        ),
        border = BorderStroke(
            width = if (isSelected) 2.dp else 1.dp,
            color = if (isSelected) Color(0xFF38BDF8) else (if (isDarkTheme) Color(0xFF334155) else Color(0xFFCBD5E1))
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 18.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = cityName,
                    style = MaterialTheme.typography.titleLarge,
                    color = textColor
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = condition,
                    style = MaterialTheme.typography.bodyMedium,
                    color = subTextColor
                )
            }
            Text(
                text = temperature,
                style = MaterialTheme.typography.headlineMedium,
                color = tempColor
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CityCardPreview() {
    IlkTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF0F172A))
                .padding(16.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                CityCard(
                    cityName = "İstanbul",
                    temperature = "24°C",
                    condition = "Güncel Hava",
                    isSelected = false
                )
                CityCard(
                    cityName = "Ankara",
                    temperature = "18°C",
                    condition = "Güncel",
                    isSelected = true
                )
            }
        }
    }
}
