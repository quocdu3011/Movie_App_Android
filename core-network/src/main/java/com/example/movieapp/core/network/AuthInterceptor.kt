package com.example.movieapp.core.network

import okhttp3.Interceptor
import okhttp3.Response

/** Adds the current bearer token to requests that do not already define auth. */
class AuthInterceptor(
    private val tokenProvider: TokenProvider,
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val token = tokenProvider.accessToken()
        if (token.isNullOrBlank() || request.header(AUTHORIZATION_HEADER) != null) {
            return chain.proceed(request)
        }
        return chain.proceed(
            request.newBuilder()
                .header(AUTHORIZATION_HEADER, "Bearer $token")
                .build(),
        )
    }

    private companion object {
        const val AUTHORIZATION_HEADER = "Authorization"
    }
}
