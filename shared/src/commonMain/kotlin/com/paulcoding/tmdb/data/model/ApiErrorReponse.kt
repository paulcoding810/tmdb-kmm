package com.paulcoding.tmdb.data.model

import kotlinx.serialization.Serializable

@Serializable
data class ApiErrorResponse(
    val status_code: Int? = null,
    val status_message: String? = null,
    val success: Boolean? = null
)