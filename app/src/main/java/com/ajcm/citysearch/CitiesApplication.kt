package com.ajcm.citysearch

import android.app.Application
import com.ajcm.citysearch.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin

class CitiesApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@CitiesApplication)
            modules(appModule)
        }
    }
}
