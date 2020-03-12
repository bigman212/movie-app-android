package ru.redmadrobot.movie_app

import android.app.Application
import ru.redmadrobot.common.di.AppProvider
import ru.redmadrobot.common.di.DaggerApplication
import ru.redmadrobot.core.network.di.component.NetworkComponent
import ru.redmadrobot.movie_app.di.DaggerAppComponent
import timber.log.Timber
import timber.log.Timber.DebugTree

class App : Application(), DaggerApplication {
    lateinit var appComponent: AppProvider

    override fun onCreate() {
        super.onCreate()

        val networkProvider = NetworkComponent.Builder.build()

        appComponent = DaggerAppComponent.factory()
            .create(this, networkProvider)

        if (BuildConfig.DEBUG) {
            Timber.plant(DebugTree())
        }
    }

    override fun getApplicationProvider(): AppProvider = appComponent
}