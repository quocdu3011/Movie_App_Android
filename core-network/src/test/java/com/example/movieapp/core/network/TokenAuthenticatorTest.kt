package com.example.movieapp.core.network

import com.example.movieapp.core.network.dto.TokenResponseDto
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import org.junit.Assert.assertEquals

import org.junit.Test
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger

class TokenAuthenticatorTest {
    @Test
    fun `refreshes only once for three concurrent unauthorized requests`() {
        val tokenProvider = FakeTokenProvider()
        val refreshCalls = AtomicInteger(0)
        val authApi = object : AuthApi {
            override fun refresh(request: RefreshTokenRequestDto): TokenResponseDto {
                refreshCalls.incrementAndGet()
                Thread.sleep(50)
                return TokenResponseDto(accessToken = "new-access-token")
            }
        }
        val authenticator = TokenAuthenticator(tokenProvider, authApi)
        val start = CountDownLatch(1)
        val executor = Executors.newFixedThreadPool(3)
        val results = (1..3).map {
            executor.submit<Request?> {
                start.await()
                authenticator.authenticate(null, unauthorizedResponse())
            }
        }

        start.countDown()
        val retriedRequests = results.map { it.get(2, TimeUnit.SECONDS) }
        executor.shutdown()

        assertEquals(1, refreshCalls.get())
        assertEquals(3, retriedRequests.count { it?.header("Authorization") == "Bearer new-access-token" })
        assertEquals("new-access-token", tokenProvider.accessToken())
    }

    private fun unauthorizedResponse(): Response = Response.Builder()
        .request(
            Request.Builder()
                .url("https://api.yourapp.com/v1/catalog/home")
                .header("Authorization", "Bearer old-access-token")
                .build(),
        )
        .protocol(Protocol.HTTP_1_1)
        .code(401)
        .message("Unauthorized")
        .build()

    private class FakeTokenProvider : TokenProvider {
        @Volatile private var accessToken: String? = "old-access-token"

        override fun accessToken(): String? = accessToken

        override fun refreshToken(): String? = "refresh-token"

        override fun saveTokens(response: TokenResponseDto) {
            accessToken = response.accessToken
        }
    }
}
