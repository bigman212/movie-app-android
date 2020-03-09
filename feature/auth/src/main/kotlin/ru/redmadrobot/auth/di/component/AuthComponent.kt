package ru.redmadrobot.auth.di.component

import dagger.Component
import ru.redmadrobot.auth.AuthActivity
import ru.redmadrobot.auth.di.module.AuthViewModelModule
import ru.redmadrobot.common.di.AppProvider
import javax.inject.Singleton

@Singleton
@Component(
    dependencies = [AppProvider::class],
    modules = [AuthViewModelModule::class]
)
interface AuthComponent {
    fun inject(activity: AuthActivity)

    class Builder private constructor() {
        companion object {
            fun build(applicationProvider: AppProvider): AuthComponent {
                return DaggerAuthComponent.builder()
                    .appProvider(applicationProvider)
                    .build()
            }
        }
    }
}

