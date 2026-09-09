package com.example.movieapp.core.network.dto

import kotlinx.serialization.Serializable

/** Standard envelope returned by the MovieApp API. */
@Serializable
data class ApiResponseDto<out T>(
    val success: Boolean,
    val data: T? = null,
    val error: ErrorResponseDto? = null,
)

/** Structured API error information. */
@Serializable
data class ErrorResponseDto(
    val code: String,
    val message: String,
)
