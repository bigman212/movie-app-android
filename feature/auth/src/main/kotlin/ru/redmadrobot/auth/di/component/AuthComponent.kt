package ru.redmadrobot.auth.di.component

import dagger.Component
import ru.redmadrobot.auth.AuthFragment
import ru.redmadrobot.auth.di.module.AuthApiModule
import ru.redmadrobot.auth.di.module.AuthViewModelModule
import ru.redmadrobot.common.di.AppProvider
import ru.redmadrobot.common.di.genre.GenreApiModule
import ru.redmadrobot.core.android.AndroidToolsProvider
import ru.redmadrobot.core.network.di.NetworkProvider

@Component(
    dependencies = [
        NetworkProvider::class,
        AndroidToolsProvider::class
    ],
    modules = [
        AuthViewModelModule::class,
        AuthApiModule::class,
        GenreApiModule::class
    ]
)
interface AuthComponent {
    fun inject(obj: AuthFragment)

    @Component.Factory
    interface Factory {
        fun create(
            networkProvider: NetworkProvider,
            androidToolsProvider: AndroidToolsProvider
        ): AuthComponent
    }

    companion object {
        fun init(appProvider: AppProvider): AuthComponent {
            return DaggerAuthComponent.factory()
                .create(appProvider, appProvider)
        }
    }
}

