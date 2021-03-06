package ru.redmadrobot.auth.di.component

import dagger.Component
import ru.redmadrobot.auth.di.module.AuthApiModule
import ru.redmadrobot.auth.di.module.AuthViewModelModule
import ru.redmadrobot.auth.login.LoginFragment
import ru.redmadrobot.auth.pin.PinCreateFragment
import ru.redmadrobot.common.di.AppProvider
import ru.redmadrobot.common.di.genre.GenreApiModule
import ru.redmadrobot.core.android.AndroidToolsProvider
import ru.redmadrobot.core.network.di.NetworkProvider
import ru.redmadrobot.persist.di.PersistenceProvider

@Component(
    dependencies = [
        NetworkProvider::class,
        AndroidToolsProvider::class,
        PersistenceProvider::class
    ],
    modules = [
        AuthViewModelModule::class,
        AuthApiModule::class,
        GenreApiModule::class
    ]
)
interface AuthComponent {
    fun inject(obj: LoginFragment)
    fun inject(obj: PinCreateFragment)

    @Component.Factory
    interface Factory {
        fun create(
            networkProvider: NetworkProvider,
            androidToolsProvider: AndroidToolsProvider,
            persistenceProvider: PersistenceProvider
        ): AuthComponent
    }

    companion object {
        fun init(appProvider: AppProvider): AuthComponent {
            return DaggerAuthComponent.factory()
                .create(appProvider, appProvider, appProvider)
        }
    }
}

