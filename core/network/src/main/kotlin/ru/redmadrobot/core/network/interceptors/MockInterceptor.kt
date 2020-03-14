package ru.redmadrobot.core.network.interceptors

import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import ru.redmadrobot.core.network.NetworkRouter
import java.net.HttpURLConnection
import javax.inject.Inject

class MockInterceptor @Inject constructor(private val router: NetworkRouter) : Interceptor {
    companion object {
        const val HEADER_CONTENT_TYPE = "content_type"
        const val MEDIA_TYPE_APPLICATION_JSON = "application/json"
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

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

    private fun makeResponseBody(data: String): ResponseBody {
        return data.toResponseBody(MEDIA_TYPE_APPLICATION_JSON.toMediaTypeOrNull())
    }

    private fun makeResponseByRequest(request: Request): String {
        val url = request.url.toUri().toString()
        return router.mockedErrorResponseByUrl(url)
    }
}