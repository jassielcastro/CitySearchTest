package com.ajcm.data_source_manager.client.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class WaterDto(
    @Json(name = "timezone_offset")
    val timezoneOffset: Int,
    @Json(name = "current")
    val currentWater: CurrentWaterDto,
)

@JsonClass(generateAdapter = true)
data class CurrentWaterDto(
    @Json(name = "sunrise")
    val sunrise: Int,
    @Json(name = "sunset")
    val sunset: Int,
    @Json(name = "temp")
    val temperature: Float,
    @Json(name = "feels_like")
    val feelsLike: Float,
    @Json(name = "humidity")
    val humidity: Int,
    @Json(name = "wind_speed")
    val windSpeed: Float,
    @Json(name = "weather")
    val waterTime: List<WaterTimeDto>,
    @Json(name = "alerts")
    val alerts: List<AlertsDto>
)

@JsonClass(generateAdapter = true)
data class WaterTimeDto(
    val main: String,
    val description: String,
)

@JsonClass(generateAdapter = true)
data class AlertsDto(
    @Json(name = "sender_name")
    val sender: String,
    @Json(name = "event")
    val event: String,
    @Json(name = "start")
    val start: Int,
    @Json(name = "end")
    val end: Int,
    @Json(name = "description")
    val description: String,
)
