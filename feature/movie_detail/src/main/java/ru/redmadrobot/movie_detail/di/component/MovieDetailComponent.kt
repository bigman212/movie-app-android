package ru.redmadrobot.movie_detail.di.component

import dagger.Component
import ru.redmadrobot.common.di.AppProvider
import ru.redmadrobot.common.di.movie.MovieApiModule
import ru.redmadrobot.common.di.vm.ViewModelFactoryModule
import ru.redmadrobot.core.android.AndroidToolsProvider
import ru.redmadrobot.core.network.di.NetworkProvider
import ru.redmadrobot.movie_detail.MovieDetailFragment
import ru.redmadrobot.movie_detail.di.module.MovieDetailViewModelModule

@Component(
    dependencies = [NetworkProvider::class, AndroidToolsProvider::class],
    modules = [
        MovieApiModule::class,

        MovieDetailViewModelModule::class,
        ViewModelFactoryModule::class
    ]
)
interface MovieDetailComponent {
    fun inject(obj: MovieDetailFragment)

    @Component.Factory
    interface Factory {
        fun create(
            networkProvider: NetworkProvider,
            androidToolsProvider: AndroidToolsProvider
        ): MovieDetailComponent
    }

    companion object {
        fun init(appProvider: AppProvider): MovieDetailComponent {
            return DaggerMovieDetailComponent.factory()
                .create(appProvider, appProvider)
        }
    }
}

