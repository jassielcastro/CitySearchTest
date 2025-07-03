package com.ajcm.data_source_manager.repository.model

data class City(
    val countryCode: String,
    val cityName: String,
    val id: Int,
    val coordinates: Coordinate,
    val favorite: Boolean
)
