package com.ajcm.citysearch.ui

import kotlinx.serialization.Serializable

@Serializable
data object Search

@Serializable
data class CityDetails(
    val cityId: String
)
