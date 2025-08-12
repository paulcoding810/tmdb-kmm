package com.paulcoding.tmdb.data.db

import com.paulcoding.tmdb.db.TMDBDatabase

class AppDatabase(database: TMDBDatabase) {
    val databaseQueries = database.databaseQueries
}