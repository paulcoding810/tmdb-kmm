package com.paulcoding.tmdb.domain.usecase

import com.paulcoding.tmdb.domain.model.Movie
import com.paulcoding.tmdb.domain.repository.MovieRepository

class SearchMoviesUseCase(
    private val repository: MovieRepository
) {
    suspend operator fun invoke(query: String): List<Movie> {
        return repository.searchMovies(query)
    }
} 