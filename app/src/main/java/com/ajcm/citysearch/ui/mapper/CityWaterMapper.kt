package com.ajcm.citysearch.ui.mapper

import com.ajcm.citysearch.ui.model.Alerts
import com.ajcm.citysearch.ui.model.CityWater
import com.ajcm.citysearch.ui.model.CurrentWater
import com.ajcm.citysearch.ui.model.WaterTime
import com.ajcm.data_source_manager.repository.model.AlertsData
import com.ajcm.data_source_manager.repository.model.CityWaterData
import com.ajcm.data_source_manager.repository.model.CurrentWaterData
import com.ajcm.data_source_manager.repository.model.WaterTimeData

fun CityWaterData.toCityWater() = CityWater(
    timezoneOffset = timezoneOffset,
    currentWater = currentWater.toCurrentWater()
)

fun CurrentWaterData.toCurrentWater() = CurrentWater(
    sunrise = sunrise,
    sunset = sunset,
    temperature = temperature,
    feelsLike = feelsLike,
    humidity = humidity,
    windSpeed = windSpeed,
    waterTime = waterTime.map { it.toWaterTime() },
    alerts = alerts.map { it.toAlert() }
)

fun WaterTimeData.toWaterTime() = WaterTime(
    main = main,
    description = description
)

fun AlertsData.toAlert() = Alerts(
    sender = sender,
    event = event,
    start = start,
    end = end,
    description = description
)
