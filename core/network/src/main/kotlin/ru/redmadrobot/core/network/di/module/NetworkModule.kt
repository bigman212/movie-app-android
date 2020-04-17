package ru.redmadrobot.core.network.di.module

import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import okhttp3.CertificatePinner
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import ru.redmadrobot.core.network.NetworkRouter
import ru.redmadrobot.core.network.adapters.MovieReleaseDateAdapter
import ru.redmadrobot.core.network.interceptors.ErrorInterceptor
import ru.redmadrobot.core.network.interceptors.HeaderInterceptor
import ru.redmadrobot.core.network.interceptors.MockInterceptor
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
object NetworkModule {
    private const val HTTP_CLIENT_TIMEOUT = 30L

    @Provides
    @Singleton
    fun provideApiClient(
        okHttpClient: OkHttpClient,
        moshiFactory: MoshiConverterFactory,
        rxJava2Adapter: RxJava2CallAdapterFactory
    ): Retrofit {
        val baseUrl = "https://" + NetworkRouter.BASE_HOSTNAME
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(baseUrl)
            .addConverterFactory(moshiFactory)
            .addCallAdapterFactory(rxJava2Adapter)
            .build()
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        headerInterceptor: HeaderInterceptor,
        errorInterceptor: ErrorInterceptor,
        loggingInterceptor: HttpLoggingInterceptor,
        mockInterceptor: MockInterceptor,

        certificatePinner: CertificatePinner
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(headerInterceptor)
            .addInterceptor(errorInterceptor)
            .addInterceptor(loggingInterceptor)
            .addInterceptor(mockInterceptor)
            .certificatePinner(certificatePinner)
            .callTimeout(HTTP_CLIENT_TIMEOUT, TimeUnit.SECONDS)
            .connectTimeout(HTTP_CLIENT_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(HTTP_CLIENT_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(HTTP_CLIENT_TIMEOUT, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    fun provideCertificatePinner(): CertificatePinner {
        val serverDns = NetworkRouter.BASE_HOSTNAME
        return CertificatePinner.Builder()
            .add(
                serverDns,
                "sha256/HkCBucsA3Tgiby96X7vjb/ojHaE1BrjvZ2+LRdJJd0E=",
                "sha256/nKWcsYrc+y5I8vLf1VGByjbt+Hnasjl+9h8lNKJytoE="
            )
            .build()
    }

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

    @Provides
    @Singleton
    fun provideMoshi(): Moshi = Moshi.Builder()
        .add(MovieReleaseDateAdapter())
        .build()

    @Provides
    @Singleton
    fun provideMoshiConverterFactory(moshi: Moshi): MoshiConverterFactory = MoshiConverterFactory.create(moshi)

    @Provides
    @Singleton
    fun provideRxJava2CallAdapterFactory(): RxJava2CallAdapterFactory = RxJava2CallAdapterFactory.create()
}
