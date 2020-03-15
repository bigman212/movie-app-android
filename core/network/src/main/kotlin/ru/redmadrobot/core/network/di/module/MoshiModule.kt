package ru.redmadrobot.core.network.di.module

import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import retrofit2.converter.moshi.MoshiConverterFactory
import ru.redmadrobot.core.network.entities.ErrorResponseAdapter
import javax.inject.Singleton

@Module
object MoshiModule {

    @Provides
    @Singleton
    fun provideMoshi(): Moshi = Moshi.Builder()
        .add(ErrorResponseAdapter())
        .build()

    @Provides
    @Singleton
    fun provideGsonConverterFactoryFactory(moshi: Moshi): MoshiConverterFactory =
        MoshiConverterFactory.create(moshi)
}