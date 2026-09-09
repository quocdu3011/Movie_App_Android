package com.example.movieapp.core.network

import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.util.concurrent.TimeUnit
import okhttp3.MediaType.Companion.toMediaType

/** Factory for configured Retrofit and OkHttp clients used by MovieApp. */
object ApiClient {
    const val BASE_URL = "https://api.yourapp.com/v1/"

    /** Creates a Retrofit instance with authentication and token refresh support. */
    fun create(
        tokenProvider: TokenProvider,
        baseUrl: String = BASE_URL,
        json: Json = defaultJson,
    ): Retrofit {
        val authApi = createRefreshApi(baseUrl, json)
        val client = httpClient(tokenProvider, authApi)
        return retrofit(baseUrl, json, client)
    }

    /** Creates a typed API service from the configured client. */
    inline fun <reified T> createService(
        tokenProvider: TokenProvider,
        baseUrl: String = BASE_URL,
        json: Json = defaultJson,
    ): T = create(tokenProvider, baseUrl, json).create(T::class.java)

    private fun createRefreshApi(baseUrl: String, json: Json): AuthApi = retrofit(
        baseUrl = baseUrl,
        json = json,
        client = httpClientWithoutAuth(),
    ).create(AuthApi::class.java)

    private fun retrofit(baseUrl: String, json: Json, client: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl(baseUrl.ensureTrailingSlash())
        .client(client)
        .addConverterFactory(json.asConverterFactory(JSON_MEDIA_TYPE))
        .build()

    private fun httpClient(
        tokenProvider: TokenProvider,
        authApi: AuthApi,
    ): OkHttpClient = httpClientWithoutAuth()
        .newBuilder()
        .addInterceptor(AuthInterceptor(tokenProvider))
        .authenticator(TokenAuthenticator(tokenProvider, authApi))
        .build()

    private fun httpClientWithoutAuth(): OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .writeTimeout(15, TimeUnit.SECONDS)
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BASIC
        })
        .build()

    private fun String.ensureTrailingSlash(): String = if (endsWith('/')) this else "$this/"

    @PublishedApi
    internal val defaultJson = Json {
        ignoreUnknownKeys = true
        isLenient = true
        explicitNulls = false
    }

    private val JSON_MEDIA_TYPE = "application/json".toMediaType()
}
