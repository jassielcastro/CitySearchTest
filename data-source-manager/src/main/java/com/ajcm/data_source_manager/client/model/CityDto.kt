package com.ajcm.data_source_manager.client.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CityDto(
    @Json(name = "country")
    val countryCode: String,
    @Json(name = "name")
    val cityName: String,
    @Json(name = "_id")
    val id: Int,
    @Json(name = "coord")
    val coordinates: CoordinateDto
)
