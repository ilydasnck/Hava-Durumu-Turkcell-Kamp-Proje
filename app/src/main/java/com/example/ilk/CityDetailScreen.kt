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
import androidx.compose.ui.graphics.luminance
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
    val isDay = true // Gece modu kapalı, her zaman gündüz gradyanı
    val isDarkTheme = MaterialTheme.colorScheme.background.luminance() < 0.5f

    val backgroundGradient = if (isLoading || isError || detailResponse == null) {
        if (isDarkTheme) {
            Brush.verticalGradient(colors = listOf(Color(0xFF0B132B), Color(0xFF1C2541), Color(0xFF2A3B66)))
        } else {
            Brush.verticalGradient(colors = listOf(Color(0xFF0284C7), Color(0xFF38BDF8), Color(0xFFBAE6FD)))
        }
    } else {
        getWeatherGradient(weatherCode, isDay, isDarkTheme)
    }

    val cardBg = Color.White.copy(alpha = if (isDarkTheme) 0.12f else 0.18f)
    val cardBorder = BorderStroke(1.dp, Color.White.copy(alpha = if (isDarkTheme) 0.25f else 0.55f))
    val textColor = if (isDarkTheme) Color.White else Color(0xFF0F172A)
    val subTextColor = if (isDarkTheme) Color.White.copy(alpha = 0.80f) else Color(0xFF475569)

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
                            Text(
                                text = city.cityName,
                                style = MaterialTheme.typography.titleLarge,
                                color = Color.White
                            )
                            Text(
                                text = "Türkiye",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.White.copy(alpha = 0.8f)
                            )
                        }
                    },
                    actions = {
                        IconButton(
                            onClick = {
                                onOpenCityList()
                            },
                            modifier = Modifier.size(48.dp)
                        ) {
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
                    val conditionText = parseWeatherCodeToText(weatherCode, isDay)
                    val temp = currentWeather?.temperature?.toInt() ?: 0

                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(20.dp)
                    ) {
                        // Hero Cam Kart (Canlı Animasyonlu)
                        item {
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(28.dp),
                                colors = CardDefaults.cardColors(containerColor = cardBg),
                                border = cardBorder
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
                                        color = textColor
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = conditionText,
                                        style = MaterialTheme.typography.titleMedium,
                                        color = subTextColor
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
                                val humidity = detail.hourly?.relativeHumidity2m?.firstOrNull()?.toInt() ?: 50
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
                            val hourlyTimes = detail.hourly?.time ?: emptyList()
                            val hourlyTemps = detail.hourly?.temperature2m?.take(24) ?: emptyList()
                            val hourlyCodes = detail.hourly?.weathercode?.take(24) ?: emptyList()

                            LazyRow(
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                itemsIndexed(hourlyTemps) { index, hourTemp ->
                                    val hourCode = hourlyCodes.getOrNull(index) ?: 0
                                    val timeStr = hourlyTimes.getOrNull(index)
                                    val hour = try {
                                        timeStr?.substringAfter("T")?.substringBefore(":")?.toInt() ?: index
                                    } catch (_: Exception) {
                                        index
                                    }
                                    val isHourDay = hour in 6..19

                                    Card(
                                        shape = RoundedCornerShape(20.dp),
                                        colors = CardDefaults.cardColors(containerColor = cardBg),
                                        border = cardBorder
                                    ) {
                                        Column(
                                            modifier = Modifier.padding(14.dp),
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            Text(
                                                text = String.format("%02d:00", hour),
                                                style = MaterialTheme.typography.labelMedium,
                                                color = subTextColor
                                            )
                                            Spacer(modifier = Modifier.height(8.dp))
                                            WeatherAnimation(
                                                weatherCode = hourCode,
                                                isDay = isHourDay,
                                                modifier = Modifier.size(38.dp)
                                            )
                                            Spacer(modifier = Modifier.height(8.dp))
                                            Text(
                                                text = "${hourTemp.toInt()}°C",
                                                style = MaterialTheme.typography.titleSmall,
                                                fontWeight = FontWeight.Bold,
                                                color = textColor
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
                                colors = CardDefaults.cardColors(containerColor = cardBg),
                                border = cardBorder
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
                                                color = textColor,
                                                modifier = Modifier.width(80.dp)
                                            )
                                            WeatherAnimation(
                                                weatherCode = dayCode,
                                                isDay = true,
                                                modifier = Modifier.size(32.dp)
                                            )
                                            Text(
                                                text = parseWeatherCodeToText(dayCode, isDay = true),
                                                style = MaterialTheme.typography.bodySmall,
                                                color = subTextColor,
                                                modifier = Modifier.weight(1f),
                                                textAlign = TextAlign.Center
                                            )
                                            Text(
                                                text = "$maxT° / $minT°",
                                                style = MaterialTheme.typography.bodyMedium,
                                                fontWeight = FontWeight.Bold,
                                                color = textColor
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
    val isDarkTheme = MaterialTheme.colorScheme.background.luminance() < 0.5f
    val cardBg = Color.White.copy(alpha = if (isDarkTheme) 0.12f else 0.18f)
    val cardBorder = BorderStroke(1.dp, Color.White.copy(alpha = if (isDarkTheme) 0.25f else 0.55f))
    val textColor = if (isDarkTheme) Color.White else Color(0xFF0F172A)
    val subTextColor = if (isDarkTheme) Color.White.copy(alpha = 0.80f) else Color(0xFF475569)

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = cardBg),
        border = cardBorder
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = CircleShape,
                color = Color.White.copy(alpha = if (isDarkTheme) 0.18f else 0.25f),
                modifier = Modifier.size(44.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = if (isDarkTheme) Color.White else Color(0xFF0284C7),
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodySmall,
                    color = subTextColor
                )
                Text(
                    text = value,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = textColor
                )
            }
        }
    }
}
