package ru.redmadrobot.core.network.di.component

import dagger.Component
import ru.redmadrobot.core.android.AndroidToolsProvider
import ru.redmadrobot.core.network.di.NetworkProvider
import ru.redmadrobot.core.network.di.module.NetworkModule
import javax.inject.Singleton

@Singleton
@Component(
    dependencies = [AndroidToolsProvider::class],
    modules = [NetworkModule::class]
)
interface NetworkComponent : NetworkProvider {
    class Builder private constructor() {
        companion object {
            fun build(androidToolsProvider: AndroidToolsProvider): NetworkProvider {
                return DaggerNetworkComponent.builder()
                    .androidToolsProvider(androidToolsProvider)
                    .build()
            }
        }
    }
}
