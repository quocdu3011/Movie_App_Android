package com.example.movieapp.core.network

import com.example.movieapp.core.network.dto.TokenResponseDto
import retrofit2.http.Body
import retrofit2.http.POST

/** Provides the current credentials used by HTTP requests. */
interface TokenProvider {
    /** Returns the current access token, or null if signed out. */
    fun accessToken(): String?

    /** Returns the refresh token, or null if it is unavailable. */
    fun refreshToken(): String?

    /** Stores freshly issued credentials. */
    fun saveTokens(response: TokenResponseDto)
}

/** Auth endpoints required by the network infrastructure. */
interface AuthApi {
    /** Exchanges a refresh token for a new access token. */
    @POST("auth/refresh")
    fun refresh(@Body request: RefreshTokenRequestDto): TokenResponseDto
}

/** Request body for the refresh-token endpoint. */
@kotlinx.serialization.Serializable
data class RefreshTokenRequestDto(
    @kotlinx.serialization.SerialName("refresh_token") val refreshToken: String,
)
