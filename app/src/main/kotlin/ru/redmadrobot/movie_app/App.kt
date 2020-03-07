package ru.redmadrobot.movie_app

import android.app.Application
import ru.redmadrobot.core.network.di.module.NetworkModule
import ru.redmadrobot.movie_app.di.AppComponent
import ru.redmadrobot.movie_app.di.AppModule
import ru.redmadrobot.movie_app.di.DaggerAppComponent

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .networkModule(NetworkModule("https://api.github.com"))
            .build()
    }

    lateinit var appComponent: AppComponent
}