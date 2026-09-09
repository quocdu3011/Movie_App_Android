package com.example.movieapp.core.network.dto

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

/** A page of API items and its pagination metadata. */
@Serializable
data class PagedResponseDto<out T>(
    val items: List<T> = emptyList(),
    val page: Int,
    @SerialName("page_size") val pageSize: Int,
    @SerialName("total_items") val totalItems: Int,
    @SerialName("total_pages") val totalPages: Int,
)

/** Tokens returned after a successful access-token refresh. */
@Serializable
data class TokenResponseDto(
    @SerialName("access_token") val accessToken: String,
    @SerialName("refresh_token") val refreshToken: String? = null,
    @SerialName("expires_in") val expiresIn: Long? = null,
)
