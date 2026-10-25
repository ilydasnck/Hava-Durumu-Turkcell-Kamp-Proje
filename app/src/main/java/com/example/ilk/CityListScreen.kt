package com.example.ilk

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.ilk.ui.theme.IlkTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CityListScreen(
    onBackClick: () -> Unit,
    onThemeChanged: () -> Unit
) {
    val context = LocalContext.current
    val onboardingManager = remember { OnboardingManager(context) }
    var defaultCityName by remember { mutableStateOf(onboardingManager.getDefaultCity()) }

    val repository = remember { WeatherRepository() }
    var cityList by remember { mutableStateOf(repository.getTurkishCities()) }

    var searchQuery by rememberSaveable { mutableStateOf("") }
    var selectedCityName by rememberSaveable(defaultCityName) { mutableStateOf<String?>(defaultCityName) }
    
    // Uygulama varsayılan şehrin detay ekranıyla başlar
    var selectedCityDetailName by rememberSaveable { mutableStateOf<String?>(defaultCityName) }
    
    var showSettingsScreen by remember { mutableStateOf(false) }
    var isNetworkError by remember { mutableStateOf(false) }

    val isDarkTheme = MaterialTheme.colorScheme.background.luminance() < 0.5f

    // Eğer ayarlar ekranı açıksa onu göster
    if (showSettingsScreen) {
        BackHandler {
            defaultCityName = onboardingManager.getDefaultCity()
            showSettingsScreen = false
        }
        SettingsScreen(
            onBackClick = {
                defaultCityName = onboardingManager.getDefaultCity()
                showSettingsScreen = false
            },
            onThemeChanged = onThemeChanged
        )
        return
    }

    val finalCityForDetail = remember(selectedCityDetailName, cityList) {
        if (selectedCityDetailName == null) {
            null
        } else {
            cityList.find { it.cityName.equals(selectedCityDetailName, ignoreCase = true) }
                ?: repository.getCityCoordinates(selectedCityDetailName!!)?.let { coords ->
                    CityWeather(
                        cityName = coords.name,
                        latitude = coords.latitude,
                        longitude = coords.longitude,
                        temperature = "...",
                        condition = "Yükleniyor...",
                        isLoading = true
                    )
                }
        }
    }

    // Eğer detay ekranı aktifse o şehri göster
    if (finalCityForDetail != null) {
        BackHandler {
            if (selectedCityDetailName != defaultCityName) {
                selectedCityDetailName = null
            } else {
                onBackClick()
            }
        }
        CityDetailScreen(
            city = finalCityForDetail,
            onOpenCityList = { selectedCityDetailName = null }
        )
        return
    }

    // Şehir listesi ekranındayken geri basıldığında varsayılan şehir detaylarına dön
    BackHandler {
        selectedCityDetailName = defaultCityName
    }

    // Arama filtreleme
    val filteredCities = cityList.filter {
        it.cityName.contains(searchQuery, ignoreCase = true)
    }

    val mainGradient = if (isDarkTheme) {
        Brush.verticalGradient(
            colors = listOf(
                Color(0xFF0F172A),
                Color(0xFF1E293B),
                Color(0xFF020617)
            )
        )
    } else {
        Brush.verticalGradient(
            colors = listOf(
                Color(0xFFF0F9FF),
                Color(0xFFE0F2FE),
                Color(0xFFBAE6FD)
            )
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(mainGradient)
    ) {
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                TopAppBar(
                    title = { Text("Şehir Arama & Keşfet", color = if (isDarkTheme) Color.White else Color(0xFF0F172A)) },
                    navigationIcon = {
                        IconButton(onClick = { selectedCityDetailName = defaultCityName }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Geri",
                                tint = if (isDarkTheme) Color.White else Color(0xFF0F172A)
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = { showSettingsScreen = true }) {
                            Icon(
                                imageVector = Icons.Default.Settings,
                                contentDescription = "Ayarlar",
                                tint = if (isDarkTheme) Color.White else Color(0xFF0F172A),
                                modifier = Modifier.size(26.dp)
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent
                    )
                )
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                // Glassmorphism Arama Kutusu
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    placeholder = { Text("Şehir ara (Örn: İstanbul)...", color = if (isDarkTheme) Color.White.copy(alpha = 0.5f) else Color(0xFF64748B)) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Ara",
                            tint = if (isDarkTheme) Color.White.copy(alpha = 0.8f) else Color(0xFF334155)
                        )
                    },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { searchQuery = "" }) {
                                Icon(
                                    imageVector = Icons.Default.Clear,
                                    contentDescription = "Temizle",
                                    tint = if (isDarkTheme) Color.White.copy(alpha = 0.8f) else Color(0xFF334155)
                                )
                            }
                        }
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = if (isDarkTheme) Color.White.copy(alpha = 0.1f) else Color.White,
                        unfocusedContainerColor = if (isDarkTheme) Color.White.copy(alpha = 0.07f) else Color.White.copy(alpha = 0.8f),
                        focusedBorderColor = Color(0xFF0284C7),
                        unfocusedBorderColor = if (isDarkTheme) Color.White.copy(alpha = 0.18f) else Color(0xFFCBD5E1),
                        focusedTextColor = if (isDarkTheme) Color.White else Color(0xFF0F172A),
                        unfocusedTextColor = if (isDarkTheme) Color.White else Color(0xFF0F172A)
                    ),
                    shape = RoundedCornerShape(20.dp),
                    singleLine = true
                )

                // Ekran Durumları
                if (isNetworkError) {
                    ConnectionErrorContent(
                        onRetry = {
                            isNetworkError = false
                            cityList = repository.getTurkishCities()
                        }
                    )
                } else if (filteredCities.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Aramanızla eşleşen şehir bulunamadı",
                            style = MaterialTheme.typography.titleMedium,
                            color = if (isDarkTheme) Color.White.copy(alpha = 0.7f) else Color(0xFF475569),
                            textAlign = TextAlign.Center
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        itemsIndexed(filteredCities, key = { index, city -> "${city.cityName}_$index" }) { _, city ->
                            val isSelected = (city.cityName == selectedCityName)

                            if (city.isLoading && !isNetworkError) {
                                LaunchedEffect(city.cityName) {
                                    val (temp, condition) = repository.fetchWeatherForCity(city.latitude, city.longitude)
                                    if (temp == "N/A" || condition == "Bağlantı Hatası") {
                                        isNetworkError = true
                                    } else {
                                        cityList = cityList.map {
                                             if (it.cityName == city.cityName) {
                                                 it.copy(temperature = temp, condition = condition, isLoading = false)
                                             } else {
                                                 it
                                             }
                                        }
                                    }
                                }
                            }

                            CityCard(
                                cityName = city.cityName,
                                temperature = city.temperature,
                                condition = if (city.cityName == defaultCityName) "Varsayılan Şehir • ${city.condition}" else city.condition,
                                isSelected = isSelected,
                                onClick = {
                                    selectedCityName = if (isSelected) null else city.cityName
                                    selectedCityDetailName = city.cityName
                                    Log.d("Şehir tıklandı", city.cityName)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ConnectionErrorContent(
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isDarkTheme = MaterialTheme.colorScheme.background.luminance() < 0.5f
    val containerColor = if (isDarkTheme) Color.White.copy(alpha = 0.08f) else Color.White
    val textColor = if (isDarkTheme) Color.White else Color(0xFF0F172A)
    val subColor = if (isDarkTheme) Color.White.copy(alpha = 0.7f) else Color(0xFF475569)

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = containerColor
            ),
            shape = RoundedCornerShape(28.dp),
            border = BorderStroke(1.dp, if (isDarkTheme) Color.White.copy(alpha = 0.18f) else Color(0xFFCBD5E1)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(28.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Surface(
                    shape = CircleShape,
                    color = if (isDarkTheme) Color.White.copy(alpha = 0.12f) else Color(0xFFE0F2FE),
                    modifier = Modifier.size(72.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = null,
                            tint = Color(0xFFF87171),
                            modifier = Modifier.size(36.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = "Opss. Bağlantı hatası oldu",
                    style = MaterialTheme.typography.titleLarge,
                    color = textColor,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Lütfen internet bağlantınızı kontrol edip tekrar deneyin.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = subColor,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(24.dp))
                Button(
                    onClick = onRetry,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF0284C7)
                    ),
                    shape = RoundedCornerShape(14.dp),
                    contentPadding = PaddingValues(horizontal = 24.dp, vertical = 12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Tekrar Deneyin",
                        style = MaterialTheme.typography.labelLarge,
                        color = Color.White
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CityListScreenPreview() {
    IlkTheme {
        CityListScreen(onBackClick = {}, onThemeChanged = {})
    }
}
