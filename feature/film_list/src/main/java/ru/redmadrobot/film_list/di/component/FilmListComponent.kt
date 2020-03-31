package ru.redmadrobot.film_list.di.component

import dagger.Component
import ru.redmadrobot.common.di.AppProvider
import ru.redmadrobot.core.android.AndroidToolsProvider
import ru.redmadrobot.core.network.di.NetworkProvider
import ru.redmadrobot.film_list.FilmListFragment
import ru.redmadrobot.film_list.di.module.FilmListViewModelModule
import ru.redmadrobot.film_list.di.module.FilmSearchServiceModule

@Component(
    dependencies = [NetworkProvider::class, AndroidToolsProvider::class],
    modules = [FilmListViewModelModule::class, FilmSearchServiceModule::class]
)
interface FilmListComponent {
    fun inject(obj: FilmListFragment)

    @Component.Factory
    interface Factory {
        fun create(
            networkProvider: NetworkProvider,
            androidToolsProvider: AndroidToolsProvider
        ): FilmListComponent
    }

    companion object {
        fun init(appProvider: AppProvider): FilmListComponent {
            return DaggerFilmListComponent.factory()
                .create(appProvider, appProvider)
        }
    }
}

