package com.paulcoding.tmdb.android

import android.app.Application
import com.paulcoding.tmdb.android.di.appModule
import com.paulcoding.tmdb.di.initKoin
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger

class TMDBApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidLogger()
            androidContext(this@TMDBApplication)
            modules(appModule)
        }
    }
} 