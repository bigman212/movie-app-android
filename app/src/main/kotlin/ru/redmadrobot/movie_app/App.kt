package ru.redmadrobot.movie_app

import android.app.Application
import ru.redmadrobot.movie_app.di.AppComponent

class App : Application() {
    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = AppComponent.Builder.build(this, "example.com")
    }
}