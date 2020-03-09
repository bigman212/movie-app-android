package ru.redmadrobot.movie_app

import android.app.Application
import ru.redmadrobot.common.di.AppProvider
import ru.redmadrobot.common.di.DaggerApplication
import ru.redmadrobot.movie_app.di.AppComponent

class App : Application(), DaggerApplication {
    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = AppComponent.Builder.build(this, "example.com")
    }

    override fun getApplicationProvider(): AppProvider = appComponent
}