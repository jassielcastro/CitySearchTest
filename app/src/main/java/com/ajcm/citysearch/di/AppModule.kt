package com.ajcm.citysearch.di

import com.ajcm.citysearch.ui.views.SharedLocationViewModel
import com.ajcm.citysearch.ui.views.details.CityDetailsViewModel
import com.ajcm.citysearch.ui.views.search.SearchViewModel
import com.ajcm.data_source_manager.repositoryModule
import com.ajcm.storage.storageModule
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import org.koin.core.context.loadKoinModules
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single<Moshi> {
        Moshi.Builder()
            .addLast(KotlinJsonAdapterFactory())
            .build()
    }

    viewModel {
        SearchViewModel(
            repository = get()
        )
    }

    viewModel {
        CityDetailsViewModel(
            repository = get(),
            waterRepository = get()
        )
    }

    viewModel {
        SharedLocationViewModel()
    }

    loadKoinModules(
        listOf(
            repositoryModule,
            storageModule
        )
    )
}
