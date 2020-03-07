package ru.redmadrobot.core.network.di

import retrofit2.Retrofit
import ru.redmadrobot.core.network.MoviesService

//только то что нам нужно?
// можно же обойтись без интерфейса и перенести методы в предка
interface NetworkProvider {
    fun apiClient(): Retrofit
    fun moviesService(): MoviesService
}