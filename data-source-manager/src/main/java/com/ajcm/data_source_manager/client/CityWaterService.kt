package com.ajcm.data_source_manager.client

import com.ajcm.data_source_manager.client.model.WeatherDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface CityWaterService {
    @GET("data/2.5/weather")
    suspend fun getCurrentCityWeatherBy(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("appid") apiKey: String,
        @Query("units") units: String = "metric"
    ): Response<WeatherDto>
}
