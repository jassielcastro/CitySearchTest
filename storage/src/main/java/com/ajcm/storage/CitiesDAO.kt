package com.ajcm.storage

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ajcm.storage.data.CityTable

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
        SELECT * FROM ${CityTable.TABLE_NAME}
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
    ): List<CityTable>

    @Query(
        """
        SELECT * FROM ${CityTable.TABLE_NAME}
        WHERE (:favorite = 0 OR favorite = 1)
          AND (:prefix == '' OR name LIKE :prefix || '%' COLLATE NOCASE)
        ORDER BY name ASC, country ASC
    """
    )
    fun getCitiesBy(
        favorite: Int,
        prefix: String,
    ): PagingSource<Int, CityTable>

    /**
     * Get a city by its ID.
     * @param cityId The ID of the city to retrieve.
     */
    @Query(
        """
        SELECT * FROM ${CityTable.TABLE_NAME}
        WHERE id = :cityId
    """
    )
    suspend fun getCityById(cityId: Int): CityTable?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCities(cities: List<CityTable>)

    @Query("UPDATE ${CityTable.TABLE_NAME} SET favorite = :isFavorite WHERE id = :cityId")
    suspend fun updateFavorite(cityId: Int, isFavorite: Boolean)
}
