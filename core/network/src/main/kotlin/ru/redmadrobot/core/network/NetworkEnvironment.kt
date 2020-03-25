package ru.redmadrobot.core.network

import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import kotlin.reflect.KClass

class NetworkEnvironment<T : Any>(apiClass: KClass<T>) {

    private val mockServer = MockWebServer().apply { start() }

    private val mockWebServerUrl = mockServer.url("").toString().removeSuffix("/")

    private val retrofit = Retrofit.Builder()
        .baseUrl(mockWebServerUrl)
        .addConverterFactory(MoshiConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()

    val api: T

    init {
        api = retrofit.create(apiClass.java)
    }

    fun dispatchResponses(pathHandler: (path: String) -> MockResponse) {
        mockServer.dispatcher = object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse {
                return pathHandler.invoke(request.path!!)
            }
        }
    }
}
