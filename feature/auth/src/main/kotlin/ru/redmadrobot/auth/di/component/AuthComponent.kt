package ru.redmadrobot.auth.di.component

import dagger.Component
import ru.redmadrobot.auth.AuthActivity
import ru.redmadrobot.auth.di.module.AuthServiceModule
import ru.redmadrobot.auth.di.module.AuthViewModelModule
import ru.redmadrobot.common.di.AppProvider
import ru.redmadrobot.core.android.AndroidToolsProvider
import ru.redmadrobot.core.network.di.NetworkProvider

@Component(
    dependencies = [NetworkProvider::class, AndroidToolsProvider::class],
    modules = [AuthViewModelModule::class, AuthServiceModule::class]
)
interface AuthComponent {
    fun inject(activity: AuthActivity)

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

