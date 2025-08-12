package com.paulcoding.tmdb.domain.usecase

import com.paulcoding.tmdb.domain.model.Movie
import com.paulcoding.tmdb.domain.repository.MovieRepository

class GetTrendingMoviesUseCase(
    private val repository: MovieRepository
) {
    suspend operator fun invoke(): List<Movie> {
        return repository.getTrendingMovies()
    }
} 