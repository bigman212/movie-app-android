package ru.redmadrobot.movie_app.di

import android.content.Context
import dagger.Module
import dagger.Provides
import ru.redmadrobot.movie_app.App
import javax.inject.Singleton

@Module
class AppModule(private val application: App) {
    @Provides
    @Singleton
    fun provideApplication(): App {
        return application
    }

    @Provides
    @Singleton
    fun provideContext(): Context {
        return application.applicationContext
    }
}