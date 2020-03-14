package ru.redmadrobot.core.network.di.component

import dagger.Component
import ru.redmadrobot.core.android.AndroidToolsProvider
import ru.redmadrobot.core.network.di.NetworkProvider
import ru.redmadrobot.core.network.di.module.MoshiModule
import ru.redmadrobot.core.network.di.module.OkHttpClientModule
import ru.redmadrobot.core.network.di.module.RetrofitModule
import javax.inject.Singleton

@Singleton
@Component(
    dependencies = [AndroidToolsProvider::class],
    modules = [RetrofitModule::class, OkHttpClientModule::class, MoshiModule::class]
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