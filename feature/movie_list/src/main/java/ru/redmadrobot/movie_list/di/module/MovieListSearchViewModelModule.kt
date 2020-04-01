package ru.redmadrobot.movie_list.di.module

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import ru.redmadrobot.common.di.ViewModelKey
import ru.redmadrobot.movie_list.search.MovieListSearchViewModel

@Module
abstract class MovieListSearchViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(MovieListSearchViewModel::class)
    internal abstract fun bindAuthViewModel(viewModel: MovieListSearchViewModel): ViewModel
}
