package com.ajcm.storage

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ajcm.storage.data.CityTable

@Database(entities = [CityTable::class], version = 1, exportSchema = false)
abstract class CitiesDataBase : RoomDatabase() {
    abstract fun citiesDao(): CitiesDAO
}
