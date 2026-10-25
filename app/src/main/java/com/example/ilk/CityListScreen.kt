package com.example.ilk

import android.util.Log
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
    
    var showSettingsScreen by remember { mutableStateOf(false) }

    // Ayarlar ekranından dönüldüğünde yeni varsayılan şehri anında al
    val defaultCityName = remember(showSettingsScreen) {
        onboardingManager.getDefaultCity()
    }

    val repository = remember { WeatherRepository() }
    var cityList by remember { mutableStateOf(repository.getTurkishCities()) }
    
    val defaultCity = remember(cityList, defaultCityName) {
        cityList.find { it.cityName == defaultCityName } ?: cityList.first()
    }

    var searchQuery by rememberSaveable { mutableStateOf("") }
    var selectedCityName by rememberSaveable(defaultCityName) { mutableStateOf<String?>(defaultCityName) }
    
    // Varsayılan olarak başlangıçta veya ayarlardan dönüldüğünde detay ekranını açıyoruz
    var selectedCityForDetail by remember(showSettingsScreen, defaultCityName) {
        mutableStateOf<CityWeather?>(defaultCity)
    }
    var isNetworkError by remember { mutableStateOf(false) }

    // Eğer ayarlar ekranı açıksa onu göster
    if (showSettingsScreen) {
        SettingsScreen(
            onBackClick = { showSettingsScreen = false },
            onThemeChanged = onThemeChanged
        )
        return
    }

    // Eğer detay ekranı açıksa o şehri göster
    if (selectedCityForDetail != null) {
        CityDetailScreen(
            city = selectedCityForDetail!!,
            onOpenCityList = { selectedCityForDetail = null }
        )
        return
    }

    // Arama filtreleme
    val filteredCities = cityList.filter {
        it.cityName.contains(searchQuery, ignoreCase = true)
    }

    val mainGradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF0F172A),
            Color(0xFF1E293B),
            Color(0xFF020617)
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(mainGradient)
    ) {
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                TopAppBar(
                    title = { Text("Şehir Arama & Keşfet", color = Color.White) },
                    navigationIcon = {
                        IconButton(onClick = { selectedCityForDetail = defaultCity }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Geri",
                                tint = Color.White
                            )
                        }
                    },
                    actions = {
                        // Ayarlar İkonu
                        IconButton(onClick = { showSettingsScreen = true }) {
                            Icon(
                                imageVector = Icons.Default.Settings,
                                contentDescription = "Ayarlar",
                                tint = Color.White,
                                modifier = Modifier.size(26.dp)
                            )
                        }
                        // + Butonu (Diğer Şehirler)
                        IconButton(onClick = { selectedCityForDetail = null }) {
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
                    placeholder = { Text("Şehir ara (Örn: İstanbul)...", color = Color.White.copy(alpha = 0.5f)) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Ara",
                            tint = Color.White.copy(alpha = 0.8f)
                        )
                    },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { searchQuery = "" }) {
                                Icon(
                                    imageVector = Icons.Default.Clear,
                                    contentDescription = "Temizle",
                                    tint = Color.White.copy(alpha = 0.8f)
                                )
                            }
                        }
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White.copy(alpha = 0.1f),
                        unfocusedContainerColor = Color.White.copy(alpha = 0.07f),
                        focusedBorderColor = Color(0xFF38BDF8),
                        unfocusedBorderColor = Color.White.copy(alpha = 0.18f),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
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
                            color = Color.White.copy(alpha = 0.7f),
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
                                    selectedCityForDetail = city
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
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = Color.White.copy(alpha = 0.08f)
            ),
            shape = RoundedCornerShape(28.dp),
            border = BorderStroke(1.dp, Color.White.copy(alpha = 0.18f)),
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
                    color = Color.White.copy(alpha = 0.12f),
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
                    color = Color.White,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Lütfen internet bağlantınızı kontrol edip tekrar deneyin.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.7f),
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
