package com.ajcm.data_source_manager.repository.mapper

import com.ajcm.data_source_manager.client.model.SunInfoDto
import com.ajcm.data_source_manager.client.model.TempInfoDto
import com.ajcm.data_source_manager.client.model.WaterDto
import com.ajcm.data_source_manager.client.model.WeatherDescriptionDto
import com.ajcm.data_source_manager.repository.model.CityWeatherData
import com.ajcm.data_source_manager.repository.model.SunInfoData
import com.ajcm.data_source_manager.repository.model.TempInfoData
import com.ajcm.data_source_manager.repository.model.WeatherDescriptionData

fun WaterDto.toData(
    images: List<String> = emptyList(),
): CityWeatherData {
    return CityWeatherData(
        timezone = timezone,
        currentWeather = weather.map { it.toData() },
        tempInfo = tempInfo.toData(),
        sunInfo = sunInfo.toData(),
        images = images
    )
}

fun WeatherDescriptionDto.toData(): WeatherDescriptionData {
    return WeatherDescriptionData(
        main = main,
        description = description
    )
}

fun TempInfoDto.toData(): TempInfoData {
    return TempInfoData(
        temperature = temperature,
        feelsLike = feelsLike,
        tempMin = tempMin,
        tempMax = tempMax
    )
}

fun SunInfoDto.toData(): SunInfoData {
    return SunInfoData(
        sunrise = sunrise,
        sunset = sunset
    )
}
