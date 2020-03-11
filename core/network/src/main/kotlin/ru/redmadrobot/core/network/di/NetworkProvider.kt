package ru.redmadrobot.core.network.di

import ru.redmadrobot.core.network.MoviesService

interface NetworkProvider {
    fun moviesService(): MoviesService
}