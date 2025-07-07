package com.ajcm.citysearch.ui.mapper

import com.ajcm.citysearch.ui.model.CityWater
import com.ajcm.citysearch.ui.model.SunInfo
import com.ajcm.citysearch.ui.model.TempInfo
import com.ajcm.citysearch.ui.model.WeatherDescription
import com.ajcm.data_source_manager.repository.model.CityWeatherData
import com.ajcm.data_source_manager.repository.model.SunInfoData
import com.ajcm.data_source_manager.repository.model.TempInfoData
import com.ajcm.data_source_manager.repository.model.WeatherDescriptionData

fun CityWeatherData.toCityWater() = CityWater(
    timezone = timezone,
    currentWeather = currentWeather.map { it.toCurrentWater() },
    tempInfo = tempInfo.toTempInfo(),
    sunInfo = sunInfo.toSunInfo(),
    images = images
)

fun WeatherDescriptionData.toCurrentWater() = WeatherDescription(
    main = main,
    description = description,
)

fun TempInfoData.toTempInfo() = TempInfo(
    temperature = temperature,
    feelsLike = feelsLike,
    tempMin = tempMin,
    tempMax = tempMax
)

fun SunInfoData.toSunInfo() = SunInfo(
    sunrise = sunrise,
    sunset = sunset
)