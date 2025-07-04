package com.ajcm.citysearch.di

import com.ajcm.data_source_manager.repositoryModule
import com.ajcm.storage.storageModule
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

val appModule = module {
    single<Moshi> {
        Moshi.Builder()
            .addLast(KotlinJsonAdapterFactory())
            .build()
    }

    loadKoinModules(
        listOf(
            repositoryModule,
            storageModule
        )
    )
}
