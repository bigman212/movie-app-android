package ru.redmadrobot.profile.di.component

import dagger.Component
import ru.redmadrobot.common.di.AppProvider
import ru.redmadrobot.core.android.AndroidToolsProvider
import ru.redmadrobot.core.network.di.NetworkProvider
import ru.redmadrobot.persist.di.PersistenceProvider
import ru.redmadrobot.profile.ProfileFragment
import ru.redmadrobot.profile.di.module.ProfileApiModule
import ru.redmadrobot.profile.di.module.ProfileViewModelModule

@Component(
    dependencies = [NetworkProvider::class, AndroidToolsProvider::class, PersistenceProvider::class],
    modules = [ProfileViewModelModule::class, ProfileApiModule::class]
)
interface ProfileComponent {
    fun inject(obj: ProfileFragment)

    @Component.Factory
    interface Factory {
        fun create(
            networkProvider: NetworkProvider,
            androidToolsProvider: AndroidToolsProvider,
            persistenceProvider: PersistenceProvider
        ): ProfileComponent
    }

    companion object {
        fun init(appProvider: AppProvider): ProfileComponent {
            return DaggerProfileComponent.factory()
                .create(appProvider, appProvider, appProvider)
        }
    }
}

