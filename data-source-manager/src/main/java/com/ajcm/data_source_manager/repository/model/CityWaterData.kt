package com.ajcm.data_source_manager.repository.model

data class CityWaterData(
    val timezoneOffset: Int,
    val currentWater: CurrentWaterData,
)

data class CurrentWaterData(
    val sunrise: Int,
    val sunset: Int,
    val temperature: Float,
    val feelsLike: Float,
    val humidity: Int,
    val windSpeed: Float,
    val waterTime: List<WaterTimeData>,
    val alerts: List<AlertsData>
)

data class WaterTimeData(
    val main: String,
    val description: String,
)

data class AlertsData(
    val sender: String,
    val event: String,
    val start: Int,
    val end: Int,
    val description: String,
)
