package com.ajcm.storage

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ajcm.storage.data.City

@Database(entities = [City::class], version = 1, exportSchema = false)
abstract class CitiesDataBase : RoomDatabase() {
    abstract fun citiesDao(): CitiesDAO
}
