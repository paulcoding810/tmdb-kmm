package com.paulcoding.tmdb.android.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.paulcoding.tmdb.android.ui.screens.MovieDetailScreen
import com.paulcoding.tmdb.android.ui.screens.MovieListScreen

@Composable
fun MovieApp() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screens.MovieListScreen) {
        composable<Screens.MovieListScreen> {
            MovieListScreen(
                onMovieClick = { movieId ->
                    navController.navigate(Screens.MovieDetailScreen(movieId))
                }
            )
        }
        composable<Screens.MovieDetailScreen> {
            MovieDetailScreen(
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}
