package com.ajcm.data_source_manager.repository

import com.ajcm.data_source_manager.client.CityWaterService
import com.ajcm.data_source_manager.repository.mapper.toData
import com.ajcm.data_source_manager.repository.model.CityWaterData

class CityWaterRepository(
    private val cityWaterService: CityWaterService,
) {
    suspend fun getWaterBy(
        latitude: Double,
        longitude: Double,
        apiKey: String
    ): Result<CityWaterData> {
        try {
            val call = cityWaterService.getCurrentCityWaterBy(
                latitude = latitude,
                longitude = longitude,
                apiKey = apiKey
            )

            if (call.isSuccessful) {
                val waterData = call.body()
                return if (waterData != null) {
                    Result.success(waterData.toData())
                } else {
                    Result.failure(Exception("No water data found"))
                }
            } else {
                return Result.failure(
                    Exception(
                        "Error fetching water data: ${
                            call.errorBody()?.string()
                        }"
                    )
                )
            }
        } catch (_: Exception) {
            return Result.failure(Exception("Error fetching water data"))
        }
    }
}
