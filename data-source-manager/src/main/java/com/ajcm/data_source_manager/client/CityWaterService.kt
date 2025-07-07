package com.ajcm.data_source_manager.client

import com.ajcm.data_source_manager.client.model.WaterDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface CityWaterService {
    @GET("data/3.0/onecall")
    suspend fun getCurrentCityWaterBy(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("exclude") exclude: String = "minutely,hourly,daily",
        @Query("appid") apiKey: String,
        @Query("units") units: String = "metric"
    ): Response<WaterDto>
}
