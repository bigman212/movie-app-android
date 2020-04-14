package ru.redmadrobot.movie_list.di.module

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import ru.redmadrobot.common.di.vm.ViewModelKey
import ru.redmadrobot.movie_list.favorite.FavoritesViewModel
import ru.redmadrobot.movie_list.search.MovieListSearchViewModel

@Module
abstract class MovieListViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(MovieListSearchViewModel::class)
    internal abstract fun bindMovieListSearchViewModel(viewModel: MovieListSearchViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(FavoritesViewModel::class)
    internal abstract fun bindFavoritesViewModel(viewModel: FavoritesViewModel): ViewModel
}
