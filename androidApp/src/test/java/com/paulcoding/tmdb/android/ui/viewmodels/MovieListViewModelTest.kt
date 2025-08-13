package com.paulcoding.tmdb.android.ui.viewmodels

import com.paulcoding.tmdb.domain.model.Movie
import com.paulcoding.tmdb.domain.usecase.GetTrendingMoviesUseCase
import com.paulcoding.tmdb.domain.usecase.SearchMoviesUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MovieListViewModelTest {

    private lateinit var viewModel: MovieListViewModel
    private lateinit var getTrendingMoviesUseCase: GetTrendingMoviesUseCase
    private lateinit var searchMoviesUseCase: SearchMoviesUseCase
    private val testDispatcher = StandardTestDispatcher()

    private val mockMovies = listOf(
        Movie(
            id = 1,
            title = "Test Movie 1",
            releaseDate = "2023-01-01",
            voteAverage = 8.5,
            posterPath = "/test1.jpg",
            backdropPath = "/backdrop1.jpg",
            overview = "Test overview 1"
        ),
        Movie(
            id = 2,
            title = "Test Movie 2",
            releaseDate = "2023-02-01",
            voteAverage = 7.8,
            posterPath = "/test2.jpg",
            backdropPath = "/backdrop2.jpg",
            overview = "Test overview 2"
        )
    )

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        getTrendingMoviesUseCase = mockk()
        searchMoviesUseCase = mockk()
        viewModel = MovieListViewModel(getTrendingMoviesUseCase, searchMoviesUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state should be correct`() = runTest {
        val initialState = viewModel.uiState.value

        assertFalse(initialState.isLoading)
        assertTrue(initialState.movies.isEmpty())
        assertNull(initialState.errorMessage)
    }

    @Test
    fun `loadTrendingMovies should update state with movies on success`() = runTest {
        coEvery { getTrendingMoviesUseCase() } returns mockMovies

        viewModel.loadTrendingMovies()
        testDispatcher.scheduler.advanceUntilIdle()

        val finalState = viewModel.uiState.value
        assertFalse(finalState.isLoading)
        assertEquals(mockMovies, finalState.movies)
        assertNull(finalState.errorMessage)
    }

    @Test
    fun `loadTrendingMovies should call use case`() = runTest {
        coEvery { getTrendingMoviesUseCase() } returns mockMovies

        viewModel.loadTrendingMovies()
        testDispatcher.scheduler.advanceUntilIdle()

        coVerify { getTrendingMoviesUseCase() }
    }

    @Test
    fun `loadTrendingMovies should handle exception and update error state`() = runTest {
        val errorMessage = "Network error"
        coEvery { getTrendingMoviesUseCase() } throws RuntimeException(errorMessage)

        viewModel.loadTrendingMovies()
        testDispatcher.scheduler.advanceUntilIdle()

        val finalState = viewModel.uiState.value
        assertFalse(finalState.isLoading)
        assertTrue(finalState.movies.isEmpty())
        assertEquals(errorMessage, finalState.errorMessage)
    }

    @Test
    fun `loadTrendingMovies should handle exception with null message`() = runTest {
        coEvery { getTrendingMoviesUseCase() } throws RuntimeException()

        viewModel.loadTrendingMovies()
        testDispatcher.scheduler.advanceUntilIdle()

        val finalState = viewModel.uiState.value
        assertFalse(finalState.isLoading)
        assertTrue(finalState.movies.isEmpty())
        assertEquals("Unknown error", finalState.errorMessage)
    }

    @Test
    fun `searchMovies should update state with movies on success`() = runTest {
        val query = "test"
        coEvery { searchMoviesUseCase(query) } returns mockMovies

        viewModel.searchMovies(query)
        testDispatcher.scheduler.advanceUntilIdle()

        val finalState = viewModel.uiState.value
        assertFalse(finalState.isLoading)
        assertEquals(mockMovies, finalState.movies)
        assertNull(finalState.errorMessage)
    }

    @Test
    fun `searchMovies should call use case with correct query`() = runTest {
        val query = "test"
        coEvery { searchMoviesUseCase(query) } returns mockMovies

        viewModel.searchMovies(query)
        testDispatcher.scheduler.advanceUntilIdle()

        coVerify { searchMoviesUseCase(query) }
    }

    @Test
    fun `searchMovies should handle exception and update error state`() = runTest {
        val query = "test"
        val errorMessage = "Search failed"
        coEvery { searchMoviesUseCase(query) } throws RuntimeException(errorMessage)

        viewModel.searchMovies(query)
        testDispatcher.scheduler.advanceUntilIdle()

        val finalState = viewModel.uiState.value
        assertFalse(finalState.isLoading)
        assertTrue(finalState.movies.isEmpty())
        assertEquals(errorMessage, finalState.errorMessage)
    }

    @Test
    fun `searchMovies should handle exception with null message`() = runTest {
        val query = "test"
        coEvery { searchMoviesUseCase(query) } throws RuntimeException()

        viewModel.searchMovies(query)
        testDispatcher.scheduler.advanceUntilIdle()

        val finalState = viewModel.uiState.value
        assertFalse(finalState.isLoading)
        assertTrue(finalState.movies.isEmpty())
        assertEquals("Unknown error", finalState.errorMessage)
    }

    @Test
    fun `searchMovies should clear previous movies when starting new search`() = runTest {
        coEvery { getTrendingMoviesUseCase() } returns mockMovies
        viewModel.loadTrendingMovies()
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(mockMovies, viewModel.uiState.value.movies)

        val query = "test"
        val searchResults = listOf(mockMovies.first())
        coEvery { searchMoviesUseCase(query) } returns searchResults

        viewModel.searchMovies(query)

        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(searchResults, viewModel.uiState.value.movies)
    }

    @Test
    fun `multiple rapid calls should not cause issues`() = runTest {
        coEvery { getTrendingMoviesUseCase() } returns mockMovies

        viewModel.loadTrendingMovies()
        viewModel.loadTrendingMovies()
        viewModel.loadTrendingMovies()

        testDispatcher.scheduler.advanceUntilIdle()

        val finalState = viewModel.uiState.value
        assertFalse(finalState.isLoading)
        assertEquals(mockMovies, finalState.movies)
        assertNull(finalState.errorMessage)
    }
} 