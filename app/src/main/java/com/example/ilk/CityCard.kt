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
    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isSelected) 10.dp else 0.dp,
            pressedElevation = 2.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) 
                Color(0xFF0284C7).copy(alpha = 0.35f) 
            else 
                Color.White.copy(alpha = 0.08f)
        ),
        border = BorderStroke(
            width = if (isSelected) 2.dp else 1.dp,
            color = if (isSelected) Color(0xFF38BDF8) else Color.White.copy(alpha = 0.18f)
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
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = condition,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.7f)
                )
            }
            Text(
                text = temperature,
                style = MaterialTheme.typography.headlineMedium,
                color = if (isSelected) Color(0xFF38BDF8) else Color.White
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
                    condition = "Güncel Hava",
                    isSelected = true
                )
            }
        }
    }
}
