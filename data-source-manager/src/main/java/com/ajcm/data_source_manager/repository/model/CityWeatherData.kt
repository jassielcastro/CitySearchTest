package com.ajcm.data_source_manager.repository.model

data class CityWeatherData(
    val timezone: Int,
    val currentWeather: List<WeatherDescriptionData>,
    val tempInfo: TempInfoData,
    val sunInfo: SunInfoData,
    val images: List<String>
)

data class WeatherDescriptionData(
    val main: String,
    val description: String,
)

data class TempInfoData(
    val temperature: Float,
    val feelsLike: Float,
    val tempMin: Float,
    val tempMax: Float,
)

data class SunInfoData(
    val sunrise: Int,
    val sunset: Int,
)

