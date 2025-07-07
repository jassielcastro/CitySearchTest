package com.ajcm.citysearch.ui.model

import androidx.compose.runtime.Immutable

@Immutable
data class CityWater(
    val timezoneOffset: Int,
    val currentWater: CurrentWater,
)

@Immutable
data class CurrentWater(
    val sunrise: Int,
    val sunset: Int,
    val temperature: Float,
    val feelsLike: Float,
    val humidity: Int,
    val windSpeed: Float,
    val waterTime: List<WaterTime>,
    val alerts: List<Alerts>,
)

@Immutable
data class WaterTime(
    val main: String,
    val description: String,
)

@Immutable
data class Alerts(
    val sender: String,
    val event: String,
    val start: Int,
    val end: Int,
    val description: String,
)
