package ru.redmadrobot.core.network.interceptors

import okhttp3.Interceptor
import okhttp3.Response

class HeaderInterceptor : Interceptor {
    companion object {
        private const val HEADER_AUTH = "Authorization"

        // репозиторий частный - апи ключ частично в безопасности
        private const val API_KEY =
            "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI5NTZjNThmZTBmMzRlYmRlNTY4YzgxMDE3NjdlNDU2NyIsInN1YiI6IjVlNjIyZTMzMjJhZjNlMDAxNWRkNTFkMCIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.01FeOl5CJ-xxwzlUkHyL-sLrfzO3Kw6XihmluiZubSI"
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
            .newBuilder()
            .addHeader(HEADER_AUTH, generateAuthHeader(API_KEY))
            .build()
        return chain.proceed(request)
    }

    private fun generateAuthHeader(token: String) = "Bearer: $token"
}