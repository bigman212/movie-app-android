package ru.redmadrobot.common.di.movie

import dagger.Module
import dagger.Provides
import dagger.Reusable
import retrofit2.Retrofit
import ru.redmadrobot.common.data.movie.MovieApi
import ru.redmadrobot.common.di.genre.GenreApiModule

@Module(includes = [GenreApiModule::class])
object MovieApiModule {
    @Provides
    @Reusable
    fun provideMovieApi(retrofit: Retrofit): MovieApi = retrofit.create(MovieApi::class.java)
}
