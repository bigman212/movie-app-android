package ru.redmadrobot.test_tools.network

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import kotlin.reflect.KClass

class NetworkEnvironment(vararg interceptors: Interceptor = arrayOf()) {
    private val mockServer = MockWebServer().apply { start() }

    private val mockWebServerUrl = mockServer.url("").toString().removeSuffix("/")

    private val httpClient: OkHttpClient

    init {
        val builder = OkHttpClient.Builder()
        interceptors.forEach {
            builder.addInterceptor(it)
        }

        httpClient = builder.build()
    }

    private val retrofit = Retrofit.Builder()
        .client(httpClient)
        .baseUrl(mockWebServerUrl)
        .addConverterFactory(MoshiConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()

    fun <T : Any> createApi(apiClass: KClass<T>): T = retrofit.create(apiClass.java)

    fun dispatchResponses(pathHandler: (path: String) -> MockResponse) {
        mockServer.dispatcher = object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse {
                return pathHandler.invoke(request.path!!)
            }
        }
    }
}
