package com.ajcm.data_source_manager.repository.model

data class CityData(
    val countryCode: String,
    val cityName: String,
    val id: Int,
    val coordinates: CoordinateData,
    val favorite: Boolean
)
