package ru.redmadrobot.movie_list.di.module

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import ru.redmadrobot.common.di.ViewModelFactoryModule
import ru.redmadrobot.common.di.ViewModelKey
import ru.redmadrobot.movie_list.search.MovieListSearchViewModel

@Module(includes = [ViewModelFactoryModule::class])
abstract class MovieListViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(MovieListSearchViewModel::class)
    internal abstract fun bindAuthViewModel(viewModel: MovieListSearchViewModel): ViewModel
}
