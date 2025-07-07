package com.ajcm.data_source_manager.repository

import com.ajcm.data_source_manager.client.CityWaterService
import com.ajcm.data_source_manager.repository.mapper.toData
import com.ajcm.data_source_manager.repository.model.CityWeatherData
import com.ajcm.data_source_manager.repository.model.SunInfoData
import com.ajcm.data_source_manager.repository.model.TempInfoData
import com.ajcm.data_source_manager.repository.model.WeatherDescriptionData

class CityWeatherRepository(
    private val cityWaterService: CityWaterService,
) {
    suspend fun getWaterBy(
        latitude: Double,
        longitude: Double,
        apiKey: String
    ): Result<CityWeatherData> {
        try {
            val call = cityWaterService.getCurrentCityWaterBy(
                latitude = latitude,
                longitude = longitude,
                apiKey = apiKey
            )

            if (call.isSuccessful) {
                val waterData = call.body()
                return if (waterData != null) {
                    Result.success(
                        waterData.toData(
                            images = generateRandomImages()
                        )
                    )
                } else {
                    Result.failure(Exception("No water data found"))
                }
            } else {
                return Result.success(generateMockDataForFailures())
            }
        } catch (error: Exception) {
            error.printStackTrace()
            return Result.failure(Exception("Error fetching water data"))
        }
    }

    private fun generateMockDataForFailures(): CityWeatherData {
        return CityWeatherData(
            timezone = 7200,
            currentWeather = listOf(
                WeatherDescriptionData(main = "Rain", description = "moderate rain")
            ),
            tempInfo = TempInfoData(
                temperature = 22.5f,
                feelsLike = 24.0f,
                tempMin = 20.0f,
                tempMax = 25.0f
            ),
            sunInfo = SunInfoData(
                sunrise = 1726636384,
                sunset = 1726680975
            ),
            images = generateRandomImages()
        )
    }

    /*
     * Generates a list of random image from https://picsum.photos/
     */
    private fun generateRandomImages(): List<String> {
        return List((5..10).random()) {
            "https://picsum.photos/400/300?random=${(1..10000).random()}"
        }
    }
}
