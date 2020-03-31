package ru.redmadrobot.film_list.di.module

import dagger.Module
import dagger.Provides
import dagger.Reusable
import retrofit2.Retrofit
import ru.redmadrobot.film_list.FilmSearchService

@Module
object FilmSearchServiceModule {
    @Provides
    @Reusable
    fun provideFilmSearchService(retrofit: Retrofit): FilmSearchService = retrofit.create(FilmSearchService::class.java)
}
