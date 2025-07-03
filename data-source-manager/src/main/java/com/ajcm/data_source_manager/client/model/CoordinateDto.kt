package com.ajcm.data_source_manager.client.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CoordinateDto(
    @Json(name = "lon")
    val longitude: Double,
    @Json(name = "lat")
    val latitude: Double
)
