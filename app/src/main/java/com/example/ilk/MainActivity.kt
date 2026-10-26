package com.example.ilk

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.example.ilk.ui.city.CityScreen
import com.example.ilk.ui.onboarding.OnboardingManager
import com.example.ilk.ui.onboarding.OnboardingScreen
import com.example.ilk.ui.theme.IlkTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("Lifecycle", "onCreate")
        enableEdgeToEdge()
        setContent {
            val context = LocalContext.current
            val onboardingManager = remember { OnboardingManager(context) }
            var isOnboardingDone by remember { mutableStateOf(onboardingManager.isOnboardingCompleted()) }
            var themeMode by remember { mutableStateOf(onboardingManager.getThemeMode()) }

            val isDark = when (themeMode) {
                "light" -> false
                "dark" -> true
                else -> isSystemInDarkTheme()
            }

            IlkTheme(darkTheme = isDark) {
                if (!isOnboardingDone) {
                    OnboardingScreen(
                        onFinishOnboarding = {
                            isOnboardingDone = true
                        }
                    )
                } else {
                    CityScreen(
                        onBackClick = { finish() },
                        onThemeChanged = {
                            themeMode = onboardingManager.getThemeMode()
                        }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainActivityPreview() {
    IlkTheme {
        CityScreen(onBackClick = {}, onThemeChanged = {})
    }
}
