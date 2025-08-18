package com.paulcoding.tmdb.data.repository

import com.paulcoding.tmdb.data.api.TmdbApi
import com.paulcoding.tmdb.data.db.AppDatabase
import com.paulcoding.tmdb.data.model.GenreDto
import com.paulcoding.tmdb.data.model.MovieDetailsDto
import com.paulcoding.tmdb.data.model.MovieDto
import com.paulcoding.tmdb.data.model.ProductionCompanyDto
import com.paulcoding.tmdb.domain.model.Genre
import com.paulcoding.tmdb.domain.model.Movie
import com.paulcoding.tmdb.domain.model.MovieDetails
import com.paulcoding.tmdb.domain.model.ProductionCompany
import com.paulcoding.tmdb.domain.repository.MovieRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import kotlin.time.Duration.Companion.hours

class MovieRepositoryImpl(
    private val api: TmdbApi,
    private val db: AppDatabase,
    private val json: Json
) : MovieRepository {

    override suspend fun getTrendingMovies(): List<Movie> = withContext(Dispatchers.IO) {
        val cache = db.databaseQueries.selectAllTrending().executeAsOneOrNull()
        return@withContext if (cache != null && isCacheFresh(cache.timestamp)) {
            parseMoviesJson(cache.data_)
        } else {
            val moviesDto = api.fetchTrendingMovies()
            val moviesJson = json.encodeToString(
                ListSerializer(MovieDto.serializer()),
                moviesDto
            )
            db.databaseQueries.insertOrReplaceTrending(moviesJson, Clock.System.now())
            moviesDto.map { it.toDomain() }
        }
    }

    override suspend fun searchMovies(query: String): List<Movie> = withContext(Dispatchers.IO) {
        val moviesDto = api.searchMovies(query)
        moviesDto.map { it.toDomain() }
    }

    override suspend fun getMovieDetails(movieId: Int): MovieDetails = withContext(Dispatchers.IO) {
        val cache = db.databaseQueries.selectByMovieId(movieId.toLong()).executeAsOneOrNull()
        return@withContext if (cache != null) {
            parseMovieDetailsJson(cache.data_)
        } else {
            val details = api.fetchMovieDetails(movieId)
            val detailsJson = json.encodeToString(
                MovieDetailsDto.serializer(),
                details
            )
            db.databaseQueries.insertOrReplaceMovieDetails(
                movieId.toLong(),
                detailsJson,
                Clock.System.now()
            )
            details.toDomain()
        }
    }

    private fun isCacheFresh(timestamp: Instant): Boolean {
        return Clock.System.now() - timestamp < 1.hours
    }

    private fun parseMoviesJson(jsonString: String): List<Movie> {
        return try {
            val moviesDto = json.decodeFromString(
                ListSerializer(MovieDto.serializer()),
                jsonString
            )
            moviesDto.map { it.toDomain() }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    private fun parseMovieDetailsJson(jsonString: String): MovieDetails {
        val detailsDto = json.decodeFromString(
            MovieDetailsDto.serializer(),
            jsonString
        )
        return detailsDto.toDomain()
    }
}

private fun MovieDto.toDomain(): Movie {
    return Movie(
        id = id,
        title = title,
        releaseDate = releaseDate,
        voteAverage = voteAverage,
        posterPath = posterPath,
        backdropPath = backdropPath,
        overview = overview
    )
}

private fun MovieDetailsDto.toDomain(): MovieDetails {
    return MovieDetails(
        id = id,
        title = title,
        releaseDate = releaseDate,
        voteAverage = voteAverage,
        posterPath = posterPath,
        backdropPath = backdropPath,
        overview = overview,
        runtime = runtime,
        genres = genres.map { it.toDomain() },
        productionCompanies = productionCompanies.map { it.toDomain() },
        homepage = homepage
    )
}

private fun GenreDto.toDomain(): Genre {
    return Genre(id = id, name = name)
}

private fun ProductionCompanyDto.toDomain(): ProductionCompany {
    return ProductionCompany(id = id, name = name, logoPath = logoPath)
}
