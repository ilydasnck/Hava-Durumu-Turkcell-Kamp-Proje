package com.example.ilk

import android.content.Context
import android.content.SharedPreferences

class OnboardingManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("weather_app_prefs", Context.MODE_PRIVATE)

    fun isOnboardingCompleted(): Boolean {
        return prefs.getBoolean("is_onboarding_completed", false)
    }

    fun setOnboardingCompleted(completed: Boolean) {
        prefs.edit().putBoolean("is_onboarding_completed", completed).apply()
    }

    fun getDefaultCity(): String {
        return prefs.getString("default_city", "İstanbul") ?: "İstanbul"
    }

    fun setDefaultCity(cityName: String) {
        prefs.edit().putString("default_city", cityName).apply()
    }

    fun getThemeMode(): String {
        return prefs.getString("theme_mode", "dark") ?: "dark"
    }

    fun setThemeMode(mode: String) {
        prefs.edit().putString("theme_mode", mode).apply()
    }
}
