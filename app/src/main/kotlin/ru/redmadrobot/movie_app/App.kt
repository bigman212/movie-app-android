package ru.redmadrobot.movie_app

import android.app.Application
import ru.redmadrobot.common.di.AppProvider
import ru.redmadrobot.common.di.DaggerApplication
import ru.redmadrobot.movie_app.di.AppComponent
import timber.log.Timber

import timber.log.Timber.DebugTree




class App : Application(), DaggerApplication {
    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = AppComponent.Builder.build(this, "http://example.com")

        if (BuildConfig.DEBUG) {
            Timber.plant(DebugTree())
        }
    }

    override fun getApplicationProvider(): AppProvider = appComponent
}