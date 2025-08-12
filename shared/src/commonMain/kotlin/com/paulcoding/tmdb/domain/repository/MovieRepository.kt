package com.paulcoding.tmdb.domain.repository

import com.paulcoding.tmdb.domain.model.Movie
import com.paulcoding.tmdb.domain.model.MovieDetails

interface MovieRepository {
    suspend fun getTrendingMovies(): List<Movie>
    suspend fun searchMovies(query: String): List<Movie>
    suspend fun getMovieDetails(movieId: Int): MovieDetails
} 