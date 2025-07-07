package com.ajcm.citysearch.ui.model

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable

@Stable
data class City(
    val id: Int,
    val countryCode: String,
    val cityName: String,
    val favorite: Boolean,
    val coordinates: Coordinate
)

@Immutable
data class Coordinate(
    val longitude: Double,
    val latitude: Double
)
