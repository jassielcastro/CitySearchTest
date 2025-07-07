package com.ajcm.data_source_manager.repository.mapper

import com.ajcm.data_source_manager.client.model.AlertsDto
import com.ajcm.data_source_manager.client.model.CurrentWaterDto
import com.ajcm.data_source_manager.client.model.WaterDto
import com.ajcm.data_source_manager.client.model.WaterTimeDto
import com.ajcm.data_source_manager.repository.model.AlertsData
import com.ajcm.data_source_manager.repository.model.CityWaterData
import com.ajcm.data_source_manager.repository.model.CurrentWaterData
import com.ajcm.data_source_manager.repository.model.WaterTimeData

fun WaterDto.toData(): CityWaterData {
    return CityWaterData(
        timezoneOffset = timezoneOffset,
        currentWater = currentWater.toData()
    )
}

fun CurrentWaterDto.toData(): CurrentWaterData {
    return CurrentWaterData(
        sunrise = sunrise,
        sunset = sunset,
        temperature = temperature,
        feelsLike = feelsLike,
        humidity = humidity,
        windSpeed = windSpeed,
        waterTime = waterTime.map { it.toData() },
        alerts = alerts.map { it.toData() }
    )
}

fun WaterTimeDto.toData(): WaterTimeData {
    return WaterTimeData(
        main = main,
        description = description
    )
}

fun AlertsDto.toData(): AlertsData {
    return AlertsData(
        sender = sender,
        event = event,
        start = start,
        end = end,
        description = description
    )
}
