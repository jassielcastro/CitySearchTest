package com.ajcm.citysearch.ui.mapper

import com.ajcm.citysearch.ui.model.City
import com.ajcm.citysearch.ui.model.Coordinate
import com.ajcm.data_source_manager.repository.model.CityData
import com.ajcm.data_source_manager.repository.model.CoordinateData

fun CityData.toCity() = City(
    id = id,
    countryCode = countryCode,
    cityName = cityName,
    favorite = favorite,
    coordinates = coordinates.toCoordinate()
)

fun CoordinateData.toCoordinate() = Coordinate(
    longitude = longitude,
    latitude = latitude
)
