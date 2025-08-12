package com.paulcoding.tmdb.domain.usecase

import com.paulcoding.tmdb.domain.model.MovieDetails
import com.paulcoding.tmdb.domain.repository.MovieRepository

class GetMovieDetailsUseCase(
    private val repository: MovieRepository
) {
    suspend operator fun invoke(movieId: Int): MovieDetails {
        return repository.getMovieDetails(movieId)
    }
} 