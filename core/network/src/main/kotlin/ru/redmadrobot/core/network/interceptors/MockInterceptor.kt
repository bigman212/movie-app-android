package ru.redmadrobot.core.network.interceptors

import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import ru.redmadrobot.core.network.MockCreator
import ru.redmadrobot.core.network.NetworkRouter
import java.net.HttpURLConnection
import javax.inject.Inject

class MockInterceptor @Inject constructor(private val mocks: MockCreator) : Interceptor {
    companion object {
        private const val HEADER_CONTENT_TYPE = "content_type"
        private const val MEDIA_TYPE_APPLICATION_JSON = "application/json"

        // нужно ли мокать запросы
        private const val MOCK_ENABLED = false
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        if (MOCK_ENABLED) {
            val mockDataResponse = makeResponseByRequest(request)
            val responseBody = makeResponseBody(mockDataResponse)

            return Response.Builder()
                .code(HttpURLConnection.HTTP_BAD_REQUEST)
                .addHeader(HEADER_CONTENT_TYPE, MEDIA_TYPE_APPLICATION_JSON)
                .message(mockDataResponse)
                .body(responseBody)
                .request(request)
                .protocol(Protocol.HTTP_2)
                .build()
        }
        return chain.proceed(request)
    }

    private fun makeResponseBody(data: String): ResponseBody {
        return data.toResponseBody(MEDIA_TYPE_APPLICATION_JSON.toMediaTypeOrNull())
    }

    /**
     * используем [NetworkRouter] для формирования мокового ответа
     * в зависимости от эндпоинта
     */
    private fun makeResponseByRequest(request: Request): String {
        val url = request.url.toUri().toString()
        return mocks.mockedErrorResponseByUrl(url)
    }
}