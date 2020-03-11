package ru.redmadrobot.auth.di.component

import dagger.Component
import ru.redmadrobot.auth.AuthActivity
import ru.redmadrobot.auth.di.module.AuthViewModelModule
import ru.redmadrobot.common.di.AppProvider
import ru.redmadrobot.core.network.di.NetworkProvider
import javax.inject.Singleton

@Component(
    dependencies = [NetworkProvider::class],
    modules = [AuthViewModelModule::class]
)
// todo(help): убрать когда разберусь со scope
// todo(help): сейчас AuthViewModelModule использует ViewModelFactory который @Singleton что порождает ошибку "may not reference bindings with different scopes"
@Singleton
interface AuthComponent {
    fun inject(activity: AuthActivity)

    class Builder private constructor() {
        companion object {
            fun build(applicationProvider: AppProvider): AuthComponent {
                return DaggerAuthComponent.builder()
                    .networkProvider(applicationProvider)
                    .build()
            }
        }
    }
}

