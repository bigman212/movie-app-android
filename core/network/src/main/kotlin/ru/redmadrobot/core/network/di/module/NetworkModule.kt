package ru.redmadrobot.core.network.di.module

import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import ru.redmadrobot.core.network.MoviesService
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
object NetworkModule {

    private const val HTTP_CLIENT_TIMEOUT = 30L
    private const val BASE_URL = "http://example.com"

    @Provides
    @Singleton
    fun provideApiClient(
        okHttpClient: OkHttpClient,
        gsonFactory: GsonConverterFactory,
        rxJava2Adapter: RxJava2CallAdapterFactory
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(gsonFactory)
            .addCallAdapterFactory(rxJava2Adapter)
            .build()
    }

    @Provides
    @Singleton
    fun provideMoviesService(retrofit: Retrofit): MoviesService = retrofit.create(MoviesService::class.java)

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.HEADERS
        }

    @Provides
    @Singleton
    fun provideOkHttpClient(loggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .callTimeout(HTTP_CLIENT_TIMEOUT, TimeUnit.SECONDS)
            .connectTimeout(HTTP_CLIENT_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(HTTP_CLIENT_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(HTTP_CLIENT_TIMEOUT, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideGsonConverterFactoryFactory(): GsonConverterFactory = GsonConverterFactory.create()

    @Provides
    @Singleton
    fun provideRxJava2CallAdapterFactory(): RxJava2CallAdapterFactory = RxJava2CallAdapterFactory.create()
}