package ru.redmadrobot.movie_app.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import ru.redmadrobot.common.di.AppProvider
import ru.redmadrobot.core.network.di.NetworkProvider
import javax.inject.Singleton

@Singleton
@Component(
    dependencies = [NetworkProvider::class]
)
interface AppComponent : AppProvider {

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance applicationContext: Context,
            networkProvider: NetworkProvider
        ): AppComponent
    }
}