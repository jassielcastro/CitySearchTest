package com.ajcm.data_source_manager.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.ajcm.data_source_manager.client.CitiesGistService
import com.ajcm.data_source_manager.repository.mapper.mapToDomain
import com.ajcm.data_source_manager.repository.mapper.mapToEntity
import com.ajcm.data_source_manager.repository.model.CityData
import com.ajcm.storage.CitiesDAO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CitiesRepository(
    private val citiesGistService: CitiesGistService,
    private val citiesDao: CitiesDAO
) {

    suspend fun areCitiesPopulated(): Boolean {
        return citiesDao.getSingle() != null
    }

    /**
     * The data is paginated and can be filtered by favorite status and prefix.
     *
     * @param favorite The favorite status to filter cities by (0 for non-favorites, 1 for favorites).
     * @param prefix The prefix to filter city names by (default is an empty string).
     * @return A [Flow] of [PagingData] containing the filtered cities.
     */
    fun getCitiesBy(
        favorite: Int,
        prefix: String = "",
    ): Flow<PagingData<CityData>> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = {
                citiesDao.getCitiesBy(favorite, prefix)
            }
        ).flow.map { pagingData ->
            pagingData.map { cityEntity ->
                cityEntity.mapToDomain()
            }
        }
    }

    suspend fun getCityById(cityId: Int) = citiesDao.getCityById(cityId)?.mapToDomain()

    suspend fun updateFavorite(cityId: Int, isFavorite: Boolean) {
        citiesDao.updateFavorite(cityId, isFavorite)
    }

    suspend fun fetchCitiesFromRemote(): Result<Unit> {
        try {
            val cities = citiesGistService.getCitiesData(CITIES_URL)
            if (cities.isSuccessful) {
                val result = cities.body()
                if (result != null && result.isNotEmpty()) {
                    citiesDao.insertCities(result.map { it.mapToEntity() })
                    return Result.success(Unit)
                }
            }
            return Result.failure(NoSuchElementException())
        } catch (_: Exception) {
            return Result.failure(NoSuchElementException())
        }
    }

    companion object {
        private const val CITIES_URL =
            "dce8843a8edbe0b0018b32e137bc2b3a/raw/0996accf70cb0ca0e16f9a99e0ee185fafca7af1/cities.json"
    }
}
