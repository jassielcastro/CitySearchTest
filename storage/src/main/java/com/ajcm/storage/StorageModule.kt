package com.ajcm.storage

import android.content.Context
import androidx.room.Room
import org.koin.dsl.module

val storageModule = module {
    single<CitiesDataBase> {
        Room.databaseBuilder(
            get<Context>(),
            CitiesDataBase::class.java,
            "cities-database"
        ).build()
    }

    single<CitiesDAO> { get<CitiesDataBase>().citiesDao() }
}
