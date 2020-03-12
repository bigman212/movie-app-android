package ru.redmadrobot.core.network.di.component

import dagger.Component
import ru.redmadrobot.core.network.di.NetworkProvider
import ru.redmadrobot.core.network.di.module.NetworkModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [NetworkModule::class]
)
interface NetworkComponent : NetworkProvider {
    class Builder private constructor() {
        companion object {
            fun build(): NetworkProvider {
                return DaggerNetworkComponent.builder()
                    .build()
            }
        }
    }
}