package com.ajcm.data_source_manager.client

import com.ajcm.data_source_manager.client.model.CityDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

interface CitiesGistService {
    @GET
    suspend fun getCitiesData(@Url url: String): Response<List<CityDto>>
}
