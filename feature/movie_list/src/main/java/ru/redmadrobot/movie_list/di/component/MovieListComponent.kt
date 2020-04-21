package ru.redmadrobot.movie_list.di.component

import dagger.Component
import ru.redmadrobot.common.di.AppProvider
import ru.redmadrobot.common.di.genre.GenreApiModule
import ru.redmadrobot.common.di.movie.MovieApiModule
import ru.redmadrobot.common.di.vm.ViewModelFactoryModule
import ru.redmadrobot.core.android.AndroidToolsProvider
import ru.redmadrobot.core.network.di.NetworkProvider
import ru.redmadrobot.movie_list.MovieListMainFragment
import ru.redmadrobot.movie_list.di.module.MovieListViewModelModule
import ru.redmadrobot.movie_list.favorite.FavoritesFragment
import ru.redmadrobot.movie_list.search.MovieListSearchFragment
import ru.redmadrobot.persist.di.PersistenceProvider

@Component(
    dependencies = [NetworkProvider::class, AndroidToolsProvider::class, PersistenceProvider::class],
    modules = [
        MovieApiModule::class,
        MovieListViewModelModule::class,
        ViewModelFactoryModule::class,
        GenreApiModule::class
    ]
)
interface MovieListComponent {
    fun inject(obj: MovieListMainFragment)
    fun inject(obj: MovieListSearchFragment)

    fun inject(obj: FavoritesFragment)


    @Component.Factory
    interface Factory {
        fun create(
            networkProvider: NetworkProvider,
            androidToolsProvider: AndroidToolsProvider,
            persistenceProvider: PersistenceProvider
        ): MovieListComponent
    }

    companion object {
        fun init(appProvider: AppProvider): MovieListComponent {
            return DaggerMovieListComponent.factory()
                .create(appProvider, appProvider, appProvider)
        }
    }
}

