package com.ajcm.citysearch

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.ajcm.citysearch.ui.theme.CitySearchTheme
import com.ajcm.citysearch.ui.MainAppContainer
import org.koin.androidx.compose.KoinAndroidContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        enableEdgeToEdge()
        setContent {
            CitySearchTheme {
                KoinAndroidContext {
                    MainAppContainer()
                }
            }
        }
    }
}
