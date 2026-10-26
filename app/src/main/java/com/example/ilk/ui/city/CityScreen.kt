package com.example.ilk.ui.city

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
import androidx.compose.ui.unit.dp
import com.example.ilk.model.City
import com.example.ilk.ui.LoadState
import com.example.ilk.ui.forecast.ForecastScreen
import com.example.ilk.ui.onboarding.OnboardingManager
import com.example.ilk.ui.settings.SettingsScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CityScreen(
    onBackClick: () -> Unit,
    onThemeChanged: () -> Unit,
    viewModel: CityViewModel = remember { CityViewModel() }
) {
    val context = LocalContext.current
    val onboardingManager = remember { OnboardingManager(context) }
    val defaultCityName = remember { onboardingManager.getDefaultCity() }

    val citiesState by viewModel.citiesState.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()

    var cityList by remember { mutableStateOf<List<City>>(emptyList()) }
    var selectedCityName by rememberSaveable(defaultCityName) { mutableStateOf<String?>(defaultCityName) }
    var selectedCityDetailName by rememberSaveable { mutableStateOf<String?>(defaultCityName) }
    var showSettingsScreen by remember { mutableStateOf(false) }

    LaunchedEffect(citiesState) {
        if (citiesState is LoadState.Success) {
            cityList = (citiesState as LoadState.Success<List<City>>).data
        }
    }

    LaunchedEffect(showSettingsScreen, defaultCityName) {
        if (!showSettingsScreen) {
            selectedCityDetailName = null
            selectedCityName = defaultCityName
        }
    }

    val isDarkTheme = MaterialTheme.colorScheme.background.luminance() < 0.5f

    if (showSettingsScreen) {
        SettingsScreen(
            onBackClick = { showSettingsScreen = false },
            onThemeChanged = onThemeChanged
        )
        return
    }

    val finalCityForDetail = remember(selectedCityDetailName, cityList) {
        if (selectedCityDetailName == null) null else cityList.find { it.cityName == selectedCityDetailName }
    }

    if (finalCityForDetail != null) {
        ForecastScreen(
            city = finalCityForDetail,
            onOpenCityList = { selectedCityDetailName = null }
        )
        return
    }

    val filteredCities = cityList.filter {
        it.cityName.contains(searchQuery, ignoreCase = true)
    }

    val mainGradient = if (isDarkTheme) {
        Brush.verticalGradient(colors = listOf(Color(0xFF0F172A), Color(0xFF1E293B), Color(0xFF020617)))
    } else {
        Brush.verticalGradient(colors = listOf(Color(0xFFF0F9FF), Color(0xFFE0F2FE), Color(0xFFBAE6FD)))
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
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { viewModel.onSearchQueryChanged(it) },
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
                            IconButton(onClick = { viewModel.onSearchQueryChanged("") }) {
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

                when (citiesState) {
                    is LoadState.Loading -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = if (isDarkTheme) Color.White else Color(0xFF0284C7))
                        }
                    }
                    is LoadState.Error -> {
                        CityErrorContent(
                            onRetry = { viewModel.loadCities() }
                        )
                    }
                    else -> {
                        if (filteredCities.isEmpty()) {
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

                                    if (city.isLoading) {
                                        LaunchedEffect(city.cityName) {
                                            val (temp, condition) = viewModel.fetchWeatherForCity(city)
                                            cityList = cityList.map {
                                                if (it.cityName == city.cityName) {
                                                    it.copy(temperature = temp, condition = condition, isLoading = false)
                                                } else {
                                                    it
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
    }
}

@Composable
fun CityErrorContent(
    onRetry: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.08f)),
            shape = RoundedCornerShape(28.dp),
            border = BorderStroke(1.dp, Color.White.copy(alpha = 0.18f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(28.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = null,
                    tint = Color(0xFFF87171),
                    modifier = Modifier.size(48.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Şehirler yüklenemedi",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = onRetry,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0284C7))
                ) {
                    Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Tekrar Deneyin")
                }
            }
        }
    }
}
