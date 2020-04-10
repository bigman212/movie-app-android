package ru.redmadrobot.movie_list.di.component

import dagger.Component
import ru.redmadrobot.common.di.AppProvider
import ru.redmadrobot.common.di.genre.GenreApiModule
import ru.redmadrobot.common.di.movie.MovieApiModule
import ru.redmadrobot.common.di.vm.ViewModelFactoryModule
import ru.redmadrobot.core.android.AndroidToolsProvider
import ru.redmadrobot.core.network.di.NetworkProvider
import ru.redmadrobot.movie_list.MovieListMainFragment
import ru.redmadrobot.movie_list.di.module.MovieListSearchViewModelModule
import ru.redmadrobot.movie_list.search.MovieListSearchFragment

@Component(
    dependencies = [NetworkProvider::class, AndroidToolsProvider::class],
    modules = [
        MovieApiModule::class,
        MovieListSearchViewModelModule::class,
        ViewModelFactoryModule::class,
        GenreApiModule::class
    ]
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

