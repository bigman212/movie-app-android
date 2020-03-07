package ru.redmadrobot.movie_app

import android.app.Application
import ru.redmadrobot.movie_app.di.AppComponent

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        appComponent = AppComponent.Builder.build(this)
    }

    lateinit var appComponent: AppComponent
}