package ru.redmadrobot.profile.di.component

import dagger.Component
import ru.redmadrobot.common.di.AppProvider
import ru.redmadrobot.core.android.AndroidToolsProvider
import ru.redmadrobot.core.network.di.NetworkProvider
import ru.redmadrobot.profile.ProfileFragment
import ru.redmadrobot.profile.di.module.ProfileServiceModule
import ru.redmadrobot.profile.di.module.ProfileViewModelModule

@Component(
    dependencies = [NetworkProvider::class, AndroidToolsProvider::class],
    modules = [ProfileViewModelModule::class, ProfileServiceModule::class]
)
interface ProfileComponent {
    fun inject(obj: ProfileFragment)

    @Component.Factory
    interface Factory {
        fun create(
            networkProvider: NetworkProvider,
            androidToolsProvider: AndroidToolsProvider
        ): ProfileComponent
    }

    companion object {
        fun init(appProvider: AppProvider): ProfileComponent {
            return DaggerProfileComponent.factory()
                .create(appProvider, appProvider)
        }
    }
}

