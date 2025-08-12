package com.paulcoding.tmdb.domain.model

data class Movie(
    val id: Int,
    val title: String,
    val releaseDate: String?,
    val voteAverage: Double,
    val posterPath: String?,
    val backdropPath: String?,
    val overview: String?
) {
    val year: String?
        get() = releaseDate?.split("-")?.firstOrNull()
    
    val posterUrl: String?
        get() = posterPath?.let { "https://image.tmdb.org/t/p/w500$it" }
    
    val backdropUrl: String?
        get() = backdropPath?.let { "https://image.tmdb.org/t/p/original$it" }
}

data class MovieDetails(
    val id: Int,
    val title: String,
    val releaseDate: String?,
    val voteAverage: Double,
    val posterPath: String?,
    val backdropPath: String?,
    val overview: String?,
    val runtime: Int?,
    val genres: List<Genre>,
    val productionCompanies: List<ProductionCompany>,
    val homepage: String?
) {
    val year: String?
        get() = releaseDate?.split("-")?.firstOrNull()
    
    val posterUrl: String?
        get() = posterPath?.let { "https://image.tmdb.org/t/p/w500$it" }
    
    val backdropUrl: String?
        get() = backdropPath?.let { "https://image.tmdb.org/t/p/original$it" }
    
    val runtimeFormatted: String?
        get() = runtime?.let { "${it} min" }
    
    val genresFormatted: String
        get() = genres.joinToString(", ") { it.name }
}

data class Genre(
    val id: Int,
    val name: String
)

data class ProductionCompany(
    val id: Int,
    val name: String,
    val logoPath: String?
) {
    val logoUrl: String?
        get() = logoPath?.let { "https://image.tmdb.org/t/p/w200$it" }
}