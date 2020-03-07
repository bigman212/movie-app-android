package ru.redmadrobot.core.network.di.component

import dagger.Component
import ru.redmadrobot.core.network.di.module.NetworkModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        NetworkModule::class
    ]
)
interface NetworkComponent