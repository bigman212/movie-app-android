package ru.redmadrobot.movie_detail.di.module

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import ru.redmadrobot.common.di.vm.ViewModelFactoryModule
import ru.redmadrobot.common.di.vm.ViewModelKey
import ru.redmadrobot.movie_detail.MovieDetailViewModel

@Module(includes = [ViewModelFactoryModule::class])
abstract class MovieDetailViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(MovieDetailViewModel::class)
    internal abstract fun bindAuthViewModel(viewModel: MovieDetailViewModel): ViewModel
}
