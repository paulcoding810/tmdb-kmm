package com.paulcoding.tmdb.android.ui.screens

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.paulcoding.tmdb.domain.model.Movie
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MovieItemTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun movieItem_displaysTitleYearAndRating_andRespondsToClick() {
        val movie = Movie(
            id = 1,
            title = "Inception",
            releaseDate = "2023-01-01",
            voteAverage = 8.8,
            posterPath = "/test1.jpg",
            backdropPath = "/backdrop1.jpg",
            overview = "Test overview 1"
        )

        var clicked = false

        composeTestRule.setContent {
            MovieItem(movie = movie, onClick = { clicked = true })
        }

        composeTestRule.onNodeWithText("Inception").assertIsDisplayed()
        composeTestRule.onNodeWithText("★ 8.8").assertIsDisplayed()

        composeTestRule.onNodeWithText("Inception").performClick()
        assert(clicked)
    }
}