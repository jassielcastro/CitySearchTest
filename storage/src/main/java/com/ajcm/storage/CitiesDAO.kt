package com.ajcm.storage

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ajcm.storage.data.City

@Dao
interface CitiesDAO {
    /**
     * Get all cities ordered by name and country.
     * @param favorite If true, only return favorite cities.
     * @param prefix If not empty, only return cities whose names start with this prefix.
     * The pagination is applied to the results, so it will return a limited number of cities by:
     * @param limit The maximum number of cities to return.
     * @param offset The number of cities to skip before starting to return results.
     */
    @Query(
        """
        SELECT * FROM ${City.TABLE_NAME}
        WHERE (:favorite = 0 OR favorite = 1)
          AND (:prefix == '' OR name LIKE :prefix || '%' COLLATE NOCASE)
        ORDER BY name ASC, country ASC
        LIMIT :limit OFFSET :offset
    """
    )
    suspend fun getCitiesBy(
        favorite: Boolean,
        prefix: String,
        limit: Int,
        offset: Int
    ): List<City>

    /**
     * Get a city by its ID.
     * @param cityId The ID of the city to retrieve.
     */
    @Query(
        """
        SELECT * FROM ${City.TABLE_NAME}
        WHERE id = :cityId
    """
    )
    suspend fun getCityById(cityId: Int): City

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCities(cities: List<City>)

    @Query("UPDATE ${City.TABLE_NAME} SET favorite = :isFavorite WHERE id = :cityId")
    suspend fun updateFavorite(cityId: Int, isFavorite: Boolean)
}
