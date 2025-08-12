package com.paulcoding.tmdb.db

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import org.koin.core.module.Module
import org.koin.dsl.module

actual val sqlDriverModule: Module = module {
    single<SqlDriver> {
        AndroidSqliteDriver(
            schema = TMDBDatabase.Schema,
            context = get(),
            name = "tmdb.db"
        )
    }
}