package ru.redmadrobot.auth.di.component

import dagger.Component
import ru.redmadrobot.auth.AuthActivity
import ru.redmadrobot.auth.di.module.AuthViewModelModule
import ru.redmadrobot.core.network.di.NetworkProvider

@Component(
    dependencies = [NetworkProvider::class],
    modules = [AuthViewModelModule::class]
)
interface AuthComponent {
    fun inject(activity: AuthActivity)

    @Component.Factory
    interface Factory {
        fun create(networkProvider: NetworkProvider): AuthComponent
    }

    companion object {
        fun init(networkProvider: NetworkProvider): AuthComponent {
            return DaggerAuthComponent.factory()
                .create(networkProvider)
        }
    }
}

