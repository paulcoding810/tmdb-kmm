package com.paulcoding.tmdb.android.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class Screens {
    @Serializable
    data object MovieListScreen : Screens()

    @Serializable
    data class MovieDetailScreen(val movieId: Int) : Screens()
}