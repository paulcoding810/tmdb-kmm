package com.paulcoding.tmdb.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MovieDto(
    val id: Int,
    val title: String,
    @SerialName("release_date")
    val releaseDate: String?,
    @SerialName("vote_average")
    val voteAverage: Double,
    @SerialName("poster_path")
    val posterPath: String?,
    @SerialName("backdrop_path")
    val backdropPath: String?,
    val overview: String?
)

@Serializable
data class MovieSearchResponse(
    val page: Int,
    val results: List<MovieDto>,
    @SerialName("total_pages")
    val totalPages: Int,
    @SerialName("total_results")
    val totalResults: Int
)

@Serializable
data class TrendingMoviesResponse(
    val page: Int,
    val results: List<MovieDto>,
    @SerialName("total_pages")
    val totalPages: Int,
    @SerialName("total_results")
    val totalResults: Int
)

@Serializable
data class MovieDetailsDto(
    val id: Int,
    val title: String,
    @SerialName("release_date")
    val releaseDate: String?,
    @SerialName("vote_average")
    val voteAverage: Double,
    @SerialName("poster_path")
    val posterPath: String?,
    @SerialName("backdrop_path")
    val backdropPath: String?,
    val overview: String?,
    val runtime: Int?,
    val genres: List<com.paulcoding.tmdb.data.model.GenreDto>,
    @SerialName("production_companies")
    val productionCompanies: List<com.paulcoding.tmdb.data.model.ProductionCompanyDto>,
    val homepage: String?
)

@Serializable
data class GenreDto(
    val id: Int,
    val name: String
)

@Serializable
data class ProductionCompanyDto(
    val id: Int,
    val name: String,
    @SerialName("logo_path")
    val logoPath: String?
)