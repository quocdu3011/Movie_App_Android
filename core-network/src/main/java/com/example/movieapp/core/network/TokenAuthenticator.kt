package com.example.movieapp.core.network

import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route

/** Refreshes expired credentials once and retries the failed request. */
class TokenAuthenticator(
    private val tokenProvider: TokenProvider,
    private val authApi: AuthApi,
) : Authenticator {
    private val lock = Any()

    override fun authenticate(route: Route?, response: Response): Request? {
        if (responseCount(response) >= MAX_AUTH_ATTEMPTS) return null

        val failedToken = response.request.header(AUTHORIZATION_HEADER)
            ?.removePrefix("Bearer ")
        synchronized(lock) {
            val currentToken = tokenProvider.accessToken()
            if (!currentToken.isNullOrBlank() && currentToken != failedToken) {
                return withBearer(response.request, currentToken)
            }

            val refreshToken = tokenProvider.refreshToken() ?: return null
            return runCatching { authApi.refresh(RefreshTokenRequestDto(refreshToken)) }
                .getOrNull()
                ?.also(tokenProvider::saveTokens)
                ?.accessToken
                ?.takeIf(String::isNotBlank)
                ?.let { withBearer(response.request, it) }
        }
    }

    private fun withBearer(request: Request, accessToken: String): Request = request.newBuilder()
        .header(AUTHORIZATION_HEADER, "Bearer $accessToken")
        .build()

    private fun responseCount(response: Response): Int {
        var count = 1
        var previous = response.priorResponse
        while (previous != null) {
            count++
            previous = previous.priorResponse
        }
        return count
    }

    private companion object {
        const val AUTHORIZATION_HEADER = "Authorization"
        const val MAX_AUTH_ATTEMPTS = 2
    }
}
