package com.ajcm.citysearch

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.ajcm.citysearch.ui.CitiesMainContainer
import com.ajcm.citysearch.ui.theme.CitySearchTheme
import org.koin.androidx.compose.KoinAndroidContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CitySearchTheme {
                KoinAndroidContext {
                    CitiesMainContainer()
                }
            }
        }
    }
}
