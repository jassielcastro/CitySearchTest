package com.ajcm.data_source_manager.repository

import com.ajcm.data_source_manager.client.CitiesGistService
import com.ajcm.data_source_manager.repository.mapper.mapToDomain
import com.ajcm.data_source_manager.repository.mapper.mapToEntity
import com.ajcm.storage.CitiesDAO

class CitiesRepository(
    private val citiesGistService: CitiesGistService,
    private val citiesDao: CitiesDAO
) {

    suspend fun isCitiesPopulated(): Boolean {
        return getCitiesBy(
            limit = 1,
            offset = 0
        ).isNotEmpty()
    }

    suspend fun getCitiesBy(
        favorite: Boolean = false,
        prefix: String = "",
        limit: Int,
        offset: Int
    ) = citiesDao.getCitiesBy(favorite, prefix, limit, offset).map {
        it.mapToDomain()
    }

    suspend fun getCityById(cityId: Int) = citiesDao.getCityById(cityId)?.mapToDomain()

    suspend fun updateFavorite(cityId: Int, isFavorite: Boolean) {
        citiesDao.updateFavorite(cityId, isFavorite)
    }

    suspend fun fetchCitiesFromRemote(): ResponseStatus<Unit> {
        try {
            val cities = citiesGistService.getCitiesData(CITIES_URL)
            if (cities.isSuccessful) {
                val result = cities.body()
                if (result != null && result.isNotEmpty()) {
                    citiesDao.insertCities(result.map { it.mapToEntity() })
                    return ResponseStatus.Success(Unit)
                }
            }
            return ResponseStatus.Error
        } catch (_: Exception) {
            return ResponseStatus.Error
        }
    }

    companion object {
        private const val CITIES_URL =
            "dce8843a8edbe0b0018b32e137bc2b3a/raw/0996accf70cb0ca0e16f9a99e0ee185fafca7af1/cities.json"
    }

    sealed class ResponseStatus<out T> {
        data class Success<T>(val result: T) : ResponseStatus<T>()
        data object Error : ResponseStatus<Nothing>()
    }
}
