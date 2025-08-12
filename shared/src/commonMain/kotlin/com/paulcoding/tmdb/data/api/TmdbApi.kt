package com.paulcoding.tmdb.data.api

import com.paulcoding.tmdb.data.model.MovieDetailsDto
import com.paulcoding.tmdb.data.model.MovieDto
import com.paulcoding.tmdb.data.model.MovieSearchResponse
import com.paulcoding.tmdb.data.model.TrendingMoviesResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class TmdbApi(private val client: HttpClient) {

    suspend fun fetchTrendingMovies(): List<MovieDto> {
        val response: TrendingMoviesResponse = client.get("trending/movie/day") {
            parameter("page", 1)
            parameter("language", "en-US")
        }.body<TrendingMoviesResponse>()
        return response.results
    }

    suspend fun searchMovies(query: String): List<MovieDto> {
        val response: MovieSearchResponse = client.get("search/movie") {
            parameter("query", query)
            parameter("page", 1)
        }.body<MovieSearchResponse>()
        return response.results
    }

    suspend fun fetchMovieDetails(movieId: Int): MovieDetailsDto {
        return client.get("movie/$movieId").body()
    }
}
