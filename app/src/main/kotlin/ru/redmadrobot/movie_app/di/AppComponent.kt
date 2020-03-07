package ru.redmadrobot.movie_app.di

import dagger.Component
import ru.redmadrobot.core.network.di.module.NetworkModule
import ru.redmadrobot.movie_app.MainActivity

import javax.inject.Singleton


@Singleton
@Component(modules = [AppModule::class, NetworkModule::class])
interface AppComponent {
    fun inject(activity: MainActivity)
}