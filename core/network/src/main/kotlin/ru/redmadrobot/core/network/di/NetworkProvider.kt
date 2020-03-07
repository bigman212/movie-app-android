package ru.redmadrobot.core.network.di

import retrofit2.Retrofit
import ru.redmadrobot.core.network.MoviesService

//только то что нам нужно?
interface NetworkProvider {
    fun provideApiClient(): Retrofit
    fun provideMoviesService(): MoviesService
}