package ru.redmadrobot.movie_list.di.module

import dagger.Module
import dagger.Provides
import dagger.Reusable
import retrofit2.Retrofit
import ru.redmadrobot.movie_list.data.MovieSearchService

@Module
object MovieSearchServiceModule {
    @Provides
    @Reusable
    fun provideMovieSearchService(retrofit: Retrofit): MovieSearchService =
        retrofit.create(MovieSearchService::class.java)
}
