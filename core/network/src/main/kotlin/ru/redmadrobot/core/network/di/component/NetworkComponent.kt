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
            fun build(baseUrl: String): NetworkProvider {
                // fixme: unresolved reference: DaggerNetworkComponent
                return DaggerNetworkComponent.builder()
                    .networkModule(NetworkModule(baseUrl))
                    .build()
            }
        }
    }
}