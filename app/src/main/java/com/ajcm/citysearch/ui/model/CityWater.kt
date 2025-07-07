package com.ajcm.citysearch.ui.model

import androidx.compose.runtime.Immutable

@Immutable
data class CityWater(
    val timezone: Int,
    val currentWeather: List<WeatherDescription>,
    val tempInfo: TempInfo,
    val sunInfo: SunInfo,
    val images: List<String>,
)

@Immutable
data class TempInfo(
    val temperature: Float,
    val feelsLike: Float,
    val tempMin: Float,
    val tempMax: Float,
)

@Immutable
data class WeatherDescription(
    val main: String,
    val description: String,
)

@Immutable
data class SunInfo(
    val sunrise: Int,
    val sunset: Int,
)
