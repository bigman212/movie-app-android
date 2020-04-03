package ru.redmadrobot.core.network.interceptors

import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class HeaderInterceptor @Inject constructor() : Interceptor {
    companion object {
        private const val HEADER_AUTH = "Authorization"

        // репозиторий частный - апи ключ частично в безопасности
        // V3 может потребоваться при отладке запросов через сайт
        private const val API_KEY_V3 = "956c58fe0f34ebde568c8101767e4567"

        private const val API_KEY_V4 =
            "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI5NTZjNThmZTBmMzRlYmRlNTY4YzgxMDE3NjdlNDU2NyIsInN1YiI6IjVlNjIyZTMzMjJhZjNlMDAxNWRkNTFkMCIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.01FeOl5CJ-xxwzlUkHyL-sLrfzO3Kw6XihmluiZubSI"

    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
            .newBuilder()
            .addHeader(HEADER_AUTH, generateAuthHeader(API_KEY_V4))
            .build()
        return chain.proceed(request)
    }

    private fun generateAuthHeader(token: String) = "Bearer $token"
}
