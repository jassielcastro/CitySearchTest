package com.ajcm.data_source_manager.client.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class WaterDto(
    @Json(name = "timezone")
    val timezone: Int,
    @Json(name = "weather")
    val weather: List<WeatherDescriptionDto>,
    @Json(name = "main")
    val tempInfo: TempInfoDto,
    @Json(name = "sys")
    val sunInfo: SunInfoDto,
)

@JsonClass(generateAdapter = true)
data class WeatherDescriptionDto(
    val main: String,
    val description: String,
)

@JsonClass(generateAdapter = true)
data class TempInfoDto(
    @Json(name = "temp")
    val temperature: Float,
    @Json(name = "feels_like")
    val feelsLike: Float,
    @Json(name = "temp_min")
    val tempMin: Float,
    @Json(name = "temp_max")
    val tempMax: Float,
)

@JsonClass(generateAdapter = true)
data class SunInfoDto(
    @Json(name = "sunrise")
    val sunrise: Int,
    @Json(name = "sunset")
    val sunset: Int,
)
