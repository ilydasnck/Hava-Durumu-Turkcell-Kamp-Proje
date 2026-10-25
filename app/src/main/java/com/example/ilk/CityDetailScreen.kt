package com.example.ilk

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CityDetailScreen(
    city: CityWeather,
    onOpenCityList: () -> Unit
) {
    val repository = remember { WeatherRepository() }
    var detailResponse by remember { mutableStateOf<WeatherDetailResponse?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var isError by remember { mutableStateOf(false) }
    var retryCount by remember { mutableIntStateOf(0) }

    // retryCount değiştikçe (Tekrar Deneyin butonuna basıldığında) isteği yeniden atar
    LaunchedEffect(city.cityName, retryCount) {
        isLoading = true
        isError = false
        val data = repository.fetchDetailedWeather(city.latitude, city.longitude)
        if (data != null) {
            detailResponse = data
            isError = false
        } else {
            isError = true
        }
        isLoading = false
    }

    val weatherCode = detailResponse?.currentWeather?.weathercode ?: 0
    val isDay = (detailResponse?.currentWeather?.isDay ?: 1) == 1
    
    // Yükleme veya Hata anında nötr gece mavisi; veri başarıyla geldiğinde havanın gerçek rengi
    val backgroundGradient = if (isLoading || isError || detailResponse == null) {
        Brush.verticalGradient(
            colors = listOf(
                Color(0xFF0F172A),
                Color(0xFF1E293B),
                Color(0xFF020617)
            )
        )
    } else {
        getWeatherGradient(weatherCode, isDay)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundGradient)
    ) {
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                TopAppBar(
                    title = {
                        Column {
                            Text(text = city.cityName, style = MaterialTheme.typography.titleLarge, color = Color.White)
                            Text(
                                text = "Türkiye",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.White.copy(alpha = 0.7f)
                            )
                        }
                    },
                    actions = {
                        // Geri gitme tuşu yerine + Butonu (Diğer Şehirler)
                        IconButton(onClick = onOpenCityList) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Şehir Ekle / Ara",
                                tint = Color.White,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent
                    )
                )
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                if (isLoading) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = Color.White)
                    }
                } else if (isError || detailResponse == null) {
                    ConnectionErrorContent(
                        onRetry = {
                            retryCount++
                        }
                    )
                } else {
                    val detail = detailResponse!!
                    val currentWeather = detail.currentWeather
                    val conditionText = parseWeatherCodeToText(weatherCode)
                    val temp = currentWeather?.temperature?.toInt() ?: 0

                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(20.dp)
                    ) {
                        // Hero Cam Kart (3D Animasyonlu & Yarı Saydam)
                        item {
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(28.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = Color.White.copy(alpha = 0.1f)
                                ),
                                border = BorderStroke(1.dp, Color.White.copy(alpha = 0.2f))
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(24.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    WeatherAnimation(
                                        weatherCode = weatherCode,
                                        isDay = isDay,
                                        modifier = Modifier.size(160.dp)
                                    )
                                    Spacer(modifier = Modifier.height(16.dp))
                                    Text(
                                        text = "$temp°C",
                                        style = MaterialTheme.typography.displayLarge.copy(
                                            fontSize = 68.sp,
                                            fontWeight = FontWeight.Bold
                                        ),
                                        color = Color.White
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = conditionText,
                                        style = MaterialTheme.typography.titleMedium,
                                        color = Color.White.copy(alpha = 0.85f)
                                    )
                                }
                            }
                        }

                        // Detaylı İstatistik Şeffaf Cam Kartlar
                        item {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                WeatherStatGlassCard(
                                    title = "Rüzgar Hızı",
                                    value = "${currentWeather?.windspeed ?: 0.0} km/s",
                                    icon = Icons.Default.Air,
                                    modifier = Modifier.weight(1f)
                                )
                                val humidity = detail.hourly?.relativeHumidity2m?.firstOrNull() ?: 50
                                WeatherStatGlassCard(
                                    title = "Nem",
                                    value = "%$humidity",
                                    icon = Icons.Default.WaterDrop,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }

                        // Saatlik Tahmin Cam Carousel
                        item {
                            Text(
                                text = "Saatlik Tahmin",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            val hourlyTemps = detail.hourly?.temperature2m?.take(24) ?: emptyList()
                            val hourlyCodes = detail.hourly?.weathercode?.take(24) ?: emptyList()

                            LazyRow(
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                itemsIndexed(hourlyTemps) { index, hourTemp ->
                                    val hourCode = hourlyCodes.getOrNull(index) ?: 0
                                    Card(
                                        shape = RoundedCornerShape(20.dp),
                                        colors = CardDefaults.cardColors(
                                            containerColor = Color.White.copy(alpha = 0.08f)
                                        ),
                                        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.15f))
                                    ) {
                                        Column(
                                            modifier = Modifier.padding(14.dp),
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            Text(
                                                text = String.format("%02d:00", index),
                                                style = MaterialTheme.typography.labelMedium,
                                                color = Color.White.copy(alpha = 0.8f)
                                            )
                                            Spacer(modifier = Modifier.height(8.dp))
                                            WeatherAnimation(
                                                weatherCode = hourCode,
                                                isDay = isDay,
                                                modifier = Modifier.size(38.dp)
                                            )
                                            Spacer(modifier = Modifier.height(8.dp))
                                            Text(
                                                text = "${hourTemp.toInt()}°C",
                                                style = MaterialTheme.typography.titleSmall,
                                                fontWeight = FontWeight.Bold,
                                                color = Color.White
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        // 7 Günlük Tahmin Cam Listesi
                        item {
                            Text(
                                text = "7 Günlük Tahmin",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            val maxTemps = detail.daily?.tempMax ?: emptyList()
                            val minTemps = detail.daily?.tempMin ?: emptyList()
                            val dailyCodes = detail.daily?.weathercode ?: emptyList()

                            Card(
                                shape = RoundedCornerShape(24.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = Color.White.copy(alpha = 0.08f)
                                ),
                                border = BorderStroke(1.dp, Color.White.copy(alpha = 0.15f))
                            ) {
                                Column(
                                    modifier = Modifier.padding(18.dp),
                                    verticalArrangement = Arrangement.spacedBy(14.dp)
                                ) {
                                    maxTemps.indices.forEach { i ->
                                        val maxT = maxTemps.getOrNull(i)?.toInt() ?: 0
                                        val minT = minTemps.getOrNull(i)?.toInt() ?: 0
                                        val dayCode = dailyCodes.getOrNull(i) ?: 0

                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = if (i == 0) "Bugün" else "${i + 1}. Gün",
                                                style = MaterialTheme.typography.bodyMedium,
                                                color = Color.White,
                                                modifier = Modifier.width(80.dp)
                                            )
                                            WeatherAnimation(
                                                weatherCode = dayCode,
                                                isDay = true,
                                                modifier = Modifier.size(32.dp)
                                            )
                                            Text(
                                                text = parseWeatherCodeToText(dayCode),
                                                style = MaterialTheme.typography.bodySmall,
                                                color = Color.White.copy(alpha = 0.7f),
                                                modifier = Modifier.weight(1f),
                                                textAlign = TextAlign.Center
                                            )
                                            Text(
                                                text = "$maxT° / $minT°",
                                                style = MaterialTheme.typography.bodyMedium,
                                                fontWeight = FontWeight.Bold,
                                                color = Color.White
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun WeatherStatGlassCard(
    title: String,
    value: String,
    icon: ImageVector,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.08f)
        ),
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.15f))
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = CircleShape,
                color = Color.White.copy(alpha = 0.12f),
                modifier = Modifier.size(44.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = Color(0xFF38BDF8),
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.White.copy(alpha = 0.7f)
                )
                Text(
                    text = value,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}
