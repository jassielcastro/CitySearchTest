package com.ajcm.data_source_manager.repository.mapper

import com.ajcm.data_source_manager.client.model.CityDto
import com.ajcm.data_source_manager.client.model.CoordinateDto
import com.ajcm.storage.data.City as CityEntity
import com.ajcm.storage.data.Coordinate as CoordinateEntity
import com.ajcm.data_source_manager.repository.model.City
import com.ajcm.data_source_manager.repository.model.Coordinate

fun CityEntity.mapToDomain(): City {
    return City(
        countryCode = this.country,
        cityName = this.name,
        id = this.id,
        coordinates = this.coordinate.mapToDomain(),
        favorite = this.favorite
    )
}

fun CoordinateEntity.mapToDomain(): Coordinate {
    return Coordinate(
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