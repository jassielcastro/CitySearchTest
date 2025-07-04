package com.ajcm.data_source_manager

import com.ajcm.data_source_manager.client.CitiesGistService
import com.ajcm.data_source_manager.repository.CitiesRepository
import com.ajcm.storage.CitiesDAO
import com.squareup.moshi.Moshi
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

const val GIST_URL_QUALIFIER = "gist_url_qualifier"

val repositoryModule = module {
    single<String>(named(GIST_URL_QUALIFIER)) {
        "https://gist.githubusercontent.com/hernan-uala/"
    }

    single<Interceptor> {
        HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }
    }

    single<OkHttpClient> {
        OkHttpClient.Builder()
            .addInterceptor(get<Interceptor>())
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    single<CitiesGistService> {
        Retrofit.Builder()
            .baseUrl(get<String>(named(GIST_URL_QUALIFIER)))
            .client(get<OkHttpClient>())
            .addConverterFactory(MoshiConverterFactory.create(get<Moshi>()))
            .build()
            .create(CitiesGistService::class.java)
    }

    single<CitiesRepository> {
        CitiesRepository(
            citiesGistService = get<CitiesGistService>(),
            citiesDao = get<CitiesDAO>()
        )
    }
}
