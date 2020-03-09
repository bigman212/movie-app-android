package ru.redmadrobot.auth.di.component

import dagger.Component
import ru.redmadrobot.auth.AuthActivity
import ru.redmadrobot.core.network.di.NetworkProvider

@Component(dependencies = [NetworkProvider::class])
interface AuthActivityComponent {
    fun inject(activity: AuthActivity)
}