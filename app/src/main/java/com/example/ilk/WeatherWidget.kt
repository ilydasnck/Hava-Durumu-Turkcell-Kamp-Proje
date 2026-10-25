package com.example.ilk

import android.content.Context
import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.action.actionStartActivity
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.*
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider

class WeatherWidget : GlanceAppWidget() {
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val onboardingManager = OnboardingManager(context)
        val defaultCityName = onboardingManager.getDefaultCity()

        val repository = WeatherRepository()
        val cityCoords = repository.getCityCoordinates(defaultCityName)
            ?: CityCoordinates("İstanbul", "Türkiye", 41.00, 28.97)

        var tempStr = "21°C"
        var conditionText = "Açık / Güneşli"
        var weatherCode = 0

        try {
            val response = ApiClient.forecastApi.getWeather(cityCoords.latitude, cityCoords.longitude)
            val cw = response.currentWeather
            if (cw != null) {
                tempStr = "${cw.temperature.toInt()}°C"
                weatherCode = cw.weathercode ?: 0
                conditionText = parseWeatherCodeToText(weatherCode)
                Log.d("WeatherWidget", "Fetched weather for $defaultCityName: $tempStr, $conditionText")
            }
        } catch (e: Exception) {
            Log.e("WeatherWidget", "Error fetching weather for widget: ${e.message}", e)
        }

        val weatherEmoji = when (weatherCode) {
            0 -> "☀️"
            1, 2 -> "🌤️"
            3, 45, 48 -> "☁️"
            in 51..82 -> "🌧️"
            in 71..86 -> "❄️"
            in 95..99 -> "⚡"
            else -> "⛅"
        }

        provideContent {
            GlanceTheme {
                Box(
                    modifier = GlanceModifier
                        .fillMaxSize()
                        .background(ColorProvider(Color.White))
                        .padding(16.dp)
                        .clickable(actionStartActivity<MainActivity>()),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        modifier = GlanceModifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.Horizontal.CenterHorizontally,
                        verticalAlignment = Alignment.Vertical.CenterVertically
                    ) {
                        Column(
                            modifier = GlanceModifier.defaultWeight(),
                            horizontalAlignment = Alignment.Horizontal.Start
                        ) {
                            Text(
                                text = defaultCityName,
                                style = TextStyle(
                                    color = ColorProvider(Color(0xFF0F172A)),
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                            Spacer(modifier = GlanceModifier.height(2.dp))
                            Text(
                                text = conditionText,
                                style = TextStyle(
                                    color = ColorProvider(Color(0xFF0284C7)),
                                    fontSize = 13.sp
                                )
                            )
                        }
                        
                        Row(
                            verticalAlignment = Alignment.Vertical.CenterVertically
                        ) {
                            Text(
                                text = weatherEmoji,
                                style = TextStyle(fontSize = 28.sp)
                            )
                            Spacer(modifier = GlanceModifier.width(8.dp))
                            Text(
                                text = tempStr,
                                style = TextStyle(
                                    color = ColorProvider(Color(0xFF0F172A)),
                                    fontSize = 26.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}

class WeatherWidgetProvider : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = WeatherWidget()
}

class WeatherWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = WeatherWidget()
}
