package ru.redmadrobot.movie_list.di.component

import dagger.Component
import ru.redmadrobot.common.di.AppProvider
import ru.redmadrobot.core.android.AndroidToolsProvider
import ru.redmadrobot.core.network.di.NetworkProvider
import ru.redmadrobot.movie_list.MovieListMainFragment
import ru.redmadrobot.movie_list.di.module.MovieListViewModelModule
import ru.redmadrobot.movie_list.di.module.MovieSearchServiceModule
import ru.redmadrobot.movie_list.search.MovieListSearchFragment

@Component(
    dependencies = [NetworkProvider::class, AndroidToolsProvider::class],
    modules = [MovieListViewModelModule::class, MovieSearchServiceModule::class]
)
interface MovieListComponent {
    fun inject(obj: MovieListMainFragment)
    fun inject(obj: MovieListSearchFragment)

    @Component.Factory
    interface Factory {
        fun create(
            networkProvider: NetworkProvider,
            androidToolsProvider: AndroidToolsProvider
        ): MovieListComponent
    }

    companion object {
        fun init(appProvider: AppProvider): MovieListComponent {
            return DaggerMovieListComponent.factory()
                .create(appProvider, appProvider)
        }
    }
}

