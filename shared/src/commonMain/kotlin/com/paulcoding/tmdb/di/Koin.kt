package com.paulcoding.tmdb.di

import app.cash.sqldelight.ColumnAdapter
import com.paulcoding.tmdb.data.api.TmdbApi
import com.paulcoding.tmdb.data.db.AppDatabase
import com.paulcoding.tmdb.data.repository.MovieRepositoryImpl
import com.paulcoding.tmdb.db.Movie_details_cache
import com.paulcoding.tmdb.db.TMDBDatabase
import com.paulcoding.tmdb.db.Trending_movie_cache
import com.paulcoding.tmdb.db.sqlDriverModule
import com.paulcoding.tmdb.domain.repository.MovieRepository
import com.paulcoding.tmdb.domain.usecase.GetMovieDetailsUseCase
import com.paulcoding.tmdb.domain.usecase.GetTrendingMoviesUseCase
import com.paulcoding.tmdb.domain.usecase.SearchMoviesUseCase
import com.paulcoding.tmdb.tmdbApiKey
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.SIMPLE
import io.ktor.client.request.header
import io.ktor.serialization.kotlinx.json.json
import kotlinx.datetime.Instant
import kotlinx.serialization.json.Json
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

val sharedModule = module {
    single {
        Json {
            ignoreUnknownKeys = true
            isLenient = true
            prettyPrint = true
        }
    }
    single {
        HttpClient {
            defaultRequest {
                url("https://api.themoviedb.org/3/")
                header("Accept", "application/json")
                header("Content-Type", "application/json")
                header(
                    "Authorization", "Bearer $tmdbApiKey"
                )
            }
            install(ContentNegotiation) {
                json(get())
            }
            install(io.ktor.client.plugins.HttpTimeout) {
                requestTimeoutMillis = 30_000
            }
            install(Logging) {
                level = LogLevel.ALL
                logger = Logger.SIMPLE
            }
        }
    }
    single { TmdbApi(get()) }
    single {
        val instantAdapter = object : ColumnAdapter<Instant, Long> {
            override fun decode(databaseValue: Long): Instant =
                Instant.fromEpochMilliseconds(databaseValue)

            override fun encode(value: Instant): Long = value.toEpochMilliseconds()
        }

        TMDBDatabase(
            get(),
            movie_details_cacheAdapter = Movie_details_cache.Adapter(
                timestampAdapter = instantAdapter
            ),
            trending_movie_cacheAdapter = Trending_movie_cache.Adapter(
                timestampAdapter = instantAdapter
            )
        )
    }
    single { AppDatabase(get()) }
    single<MovieRepository> {
        MovieRepositoryImpl(
            get(),
            get(),
            get()
        )
    }
    single { GetTrendingMoviesUseCase(get()) }
    single { SearchMoviesUseCase(get()) }
    single { GetMovieDetailsUseCase(get()) }
}

fun initKoin(appDeclaration: KoinAppDeclaration = {}) =
    startKoin {
        modules(sqlDriverModule)
        modules(sharedModule)
        appDeclaration()
    }