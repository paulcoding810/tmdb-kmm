package com.paulcoding.tmdb.db

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import org.koin.core.module.Module
import org.koin.dsl.module

actual val sqlDriverModule: Module = module {
    single<SqlDriver> {
        NativeSqliteDriver(
            schema = TMDBDatabase.Schema,
            name = "tmdb.db"
        )
    }
}