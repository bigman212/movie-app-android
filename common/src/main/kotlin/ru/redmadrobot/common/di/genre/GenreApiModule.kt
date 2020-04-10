package ru.redmadrobot.common.di.genre

import dagger.Module
import dagger.Provides
import dagger.Reusable
import retrofit2.Retrofit
import ru.redmadrobot.common.data.genre.GenreApi

@Module
object GenreApiModule {
    @Provides
    @Reusable
    fun provideGenreService(retrofit: Retrofit): GenreApi = retrofit.create(GenreApi::class.java)
}
