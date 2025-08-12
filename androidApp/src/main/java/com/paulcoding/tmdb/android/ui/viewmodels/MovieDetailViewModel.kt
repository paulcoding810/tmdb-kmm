package com.paulcoding.tmdb.android.ui.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.paulcoding.tmdb.android.ui.navigation.Screens
import com.paulcoding.tmdb.domain.model.MovieDetails
import com.paulcoding.tmdb.domain.usecase.GetMovieDetailsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class MovieDetailUiState {
    data object Loading : MovieDetailUiState()
    data class Success(val movieDetails: MovieDetails) : MovieDetailUiState()
    data class Error(val message: String) : MovieDetailUiState()
}

class MovieDetailViewModel(
    private val getMovieDetailsUseCase: GetMovieDetailsUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val movieId = savedStateHandle.toRoute<Screens.MovieDetailScreen>().movieId

    private val _uiState = MutableStateFlow<MovieDetailUiState>(MovieDetailUiState.Loading)
    val uiState: StateFlow<MovieDetailUiState> = _uiState.asStateFlow()

    fun loadMovieDetails() {
        viewModelScope.launch {
            _uiState.value = MovieDetailUiState.Loading
            try {
                val movieDetails = getMovieDetailsUseCase(movieId)
                _uiState.value = MovieDetailUiState.Success(movieDetails)
            } catch (e: Exception) {
                e.printStackTrace()
                _uiState.value = MovieDetailUiState.Error(e.message ?: "Unknown error")
            }
        }
    }
} 