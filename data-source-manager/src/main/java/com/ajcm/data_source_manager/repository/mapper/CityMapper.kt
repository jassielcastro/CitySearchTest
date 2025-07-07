package com.ajcm.data_source_manager.repository.mapper

import com.ajcm.data_source_manager.client.model.CityDto
import com.ajcm.data_source_manager.client.model.CoordinateDto
import com.ajcm.storage.data.CityTable as CityEntity
import com.ajcm.storage.data.CoordinateEmb as CoordinateEntity
import com.ajcm.data_source_manager.repository.model.CityData
import com.ajcm.data_source_manager.repository.model.CoordinateData

fun CityEntity.mapToDomain(): CityData {
    return CityData(
        countryCode = this.country,
        cityName = this.name,
        id = this.id,
        coordinates = this.coordinate.mapToDomain(),
        favorite = this.favorite
    )
}

fun CoordinateEntity.mapToDomain(): CoordinateData {
    return CoordinateData(
        latitude = this.latitude,
        longitude = this.longitude
    )
}


fun CityDto.mapToEntity(): CityEntity {
    return CityEntity(
        id = this.id,
        country = this.countryCode,
        name = this.cityName,
        coordinate = this.coordinates.mapToEntity(),
        favorite = false
    )
}

fun CoordinateDto.mapToEntity(): CoordinateEntity {
    return CoordinateEntity(
        latitude = this.latitude,
        longitude = this.longitude
    )
}