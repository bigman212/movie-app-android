package ru.redmadrobot.core.network.di.module

import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import ru.redmadrobot.core.network.interceptors.ErrorInterceptor
import ru.redmadrobot.core.network.interceptors.HeaderInterceptor
import ru.redmadrobot.core.network.interceptors.MockInterceptor
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
object OkHttpClientModule {

    private const val HTTP_CLIENT_TIMEOUT = 30L

    @Provides
    @Singleton
    fun provideOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor, mockInterceptor: MockInterceptor,
        errorInterceptor: ErrorInterceptor, headerInterceptor: HeaderInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(headerInterceptor)
            .addInterceptor(errorInterceptor)
            .addInterceptor(loggingInterceptor)
            .addInterceptor(mockInterceptor)
            .callTimeout(HTTP_CLIENT_TIMEOUT, TimeUnit.SECONDS)
            .connectTimeout(HTTP_CLIENT_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(HTTP_CLIENT_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(HTTP_CLIENT_TIMEOUT, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
}