package com.paulcoding.tmdb.android.ui.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import com.paulcoding.tmdb.android.ui.navigation.Screens
import com.paulcoding.tmdb.domain.model.Genre
import com.paulcoding.tmdb.domain.model.MovieDetails
import com.paulcoding.tmdb.domain.model.ProductionCompany
import com.paulcoding.tmdb.domain.usecase.GetMovieDetailsUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MovieDetailViewModelTest {

    private lateinit var viewModel: MovieDetailViewModel
    private lateinit var getMovieDetailsUseCase: GetMovieDetailsUseCase
    private lateinit var savedStateHandle: SavedStateHandle
    private val testDispatcher = StandardTestDispatcher()

    private val movieId = 123
    private val mockMovieDetails = MovieDetails(
        id = movieId,
        title = "Test Movie",
        releaseDate = "2023-01-01",
        voteAverage = 8.5,
        posterPath = "/test.jpg",
        backdropPath = "/backdrop.jpg",
        overview = "Test overview",
        runtime = 120,
        genres = listOf(
            Genre(id = 1, name = "Action"),
            Genre(id = 2, name = "Drama")
        ),
        productionCompanies = listOf(
            ProductionCompany(
                id = 1,
                name = "Test Studio",
                logoPath = "/logo.jpg"
            )
        ),
        homepage = "https://example.com"
    )

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        getMovieDetailsUseCase = mockk()

        savedStateHandle = mockk<SavedStateHandle>()

        mockkStatic("androidx.navigation.SavedStateHandleKt")
        every { savedStateHandle.toRoute<Screens.MovieDetailScreen>() } returns Screens.MovieDetailScreen(
            movieId
        )

        viewModel = MovieDetailViewModel(getMovieDetailsUseCase, savedStateHandle)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        unmockkStatic("androidx.navigation.SavedStateHandleKt")
    }

    @Test
    fun `initial state should be Loading`() = runTest {
        val initialState = viewModel.uiState.value

        assertTrue(initialState is MovieDetailUiState.Loading)
    }

    @Test
    fun `loadMovieDetails should update state to Loading initially`() = runTest {
        coEvery { getMovieDetailsUseCase(movieId) } returns mockMovieDetails

        viewModel.loadMovieDetails()

        assertTrue(viewModel.uiState.value is MovieDetailUiState.Loading)
    }

    @Test
    fun `loadMovieDetails should update state with movie details on success`() = runTest {
        coEvery { getMovieDetailsUseCase(movieId) } returns mockMovieDetails

        viewModel.loadMovieDetails()
        testDispatcher.scheduler.advanceUntilIdle()

        val finalState = viewModel.uiState.value
        assertTrue(finalState is MovieDetailUiState.Success)

        val successState = finalState as MovieDetailUiState.Success
        assertEquals(mockMovieDetails, successState.movieDetails)
    }

    @Test
    fun `loadMovieDetails should call use case with correct movie ID`() = runTest {
        coEvery { getMovieDetailsUseCase(movieId) } returns mockMovieDetails

        viewModel.loadMovieDetails()
        testDispatcher.scheduler.advanceUntilIdle()

        coVerify { getMovieDetailsUseCase(movieId) }
    }

    @Test
    fun `loadMovieDetails should handle exception and update error state`() = runTest {
        val errorMessage = "Network error"
        coEvery { getMovieDetailsUseCase(movieId) } throws RuntimeException(errorMessage)

        viewModel.loadMovieDetails()
        testDispatcher.scheduler.advanceUntilIdle()

        val finalState = viewModel.uiState.value
        assertTrue(finalState is MovieDetailUiState.Error)

        val errorState = finalState as MovieDetailUiState.Error
        assertEquals(errorMessage, errorState.message)
    }

    @Test
    fun `loadMovieDetails should handle exception with null message`() = runTest {
        coEvery { getMovieDetailsUseCase(movieId) } throws RuntimeException()

        viewModel.loadMovieDetails()
        testDispatcher.scheduler.advanceUntilIdle()

        val finalState = viewModel.uiState.value
        assertTrue(finalState is MovieDetailUiState.Error)

        val errorState = finalState as MovieDetailUiState.Error
        assertEquals("Unknown error", errorState.message)
    }

    @Test
    fun `multiple rapid calls should not cause issues`() = runTest {
        coEvery { getMovieDetailsUseCase(movieId) } returns mockMovieDetails

        viewModel.loadMovieDetails()
        viewModel.loadMovieDetails()
        viewModel.loadMovieDetails()

        testDispatcher.scheduler.advanceUntilIdle()

        val finalState = viewModel.uiState.value
        assertTrue(finalState is MovieDetailUiState.Success)

        val successState = finalState as MovieDetailUiState.Success
        assertEquals(mockMovieDetails, successState.movieDetails)
    }

    @Test
    fun `movie details computed properties should work correctly`() = runTest {
        coEvery { getMovieDetailsUseCase(movieId) } returns mockMovieDetails

        viewModel.loadMovieDetails()
        testDispatcher.scheduler.advanceUntilIdle()

        val finalState = viewModel.uiState.value as MovieDetailUiState.Success
        val movieDetails = finalState.movieDetails

        assertEquals("2023", movieDetails.year)
        assertEquals("https://image.tmdb.org/t/p/w500/test.jpg", movieDetails.posterUrl)
        assertEquals("https://image.tmdb.org/t/p/original/backdrop.jpg", movieDetails.backdropUrl)
        assertEquals("120 min", movieDetails.runtimeFormatted)
        assertEquals("Action, Drama", movieDetails.genresFormatted)
    }

    @Test
    fun `movie details with null values should handle gracefully`() = runTest {
        val movieDetailsWithNulls = MovieDetails(
            id = movieId,
            title = "Test Movie",
            releaseDate = null,
            voteAverage = 8.5,
            posterPath = null,
            backdropPath = null,
            overview = null,
            runtime = null,
            genres = emptyList(),
            productionCompanies = emptyList(),
            homepage = null
        )

        coEvery { getMovieDetailsUseCase(movieId) } returns movieDetailsWithNulls

        viewModel.loadMovieDetails()
        testDispatcher.scheduler.advanceUntilIdle()

        val finalState = viewModel.uiState.value as MovieDetailUiState.Success
        val movieDetails = finalState.movieDetails

        assertNull(movieDetails.year)
        assertNull(movieDetails.posterUrl)
        assertNull(movieDetails.backdropUrl)
        assertNull(movieDetails.runtimeFormatted)
        assertEquals("", movieDetails.genresFormatted)
    }

    @Test
    fun `production company logo URL should be computed correctly`() = runTest {
        val movieDetailsWithLogo = MovieDetails(
            id = movieId,
            title = "Test Movie",
            releaseDate = "2023-01-01",
            voteAverage = 8.5,
            posterPath = "/test.jpg",
            backdropPath = "/backdrop.jpg",
            overview = "Test overview",
            runtime = 120,
            genres = listOf(Genre(id = 1, name = "Action")),
            productionCompanies = listOf(
                ProductionCompany(
                    id = 1,
                    name = "Test Studio",
                    logoPath = "/logo.jpg"
                )
            ),
            homepage = "https://example.com"
        )

        coEvery { getMovieDetailsUseCase(movieId) } returns movieDetailsWithLogo

        viewModel.loadMovieDetails()
        testDispatcher.scheduler.advanceUntilIdle()

        val finalState = viewModel.uiState.value as MovieDetailUiState.Success
        val productionCompany = finalState.movieDetails.productionCompanies.first()

        assertEquals("https://image.tmdb.org/t/p/w200/logo.jpg", productionCompany.logoUrl)
    }
}
