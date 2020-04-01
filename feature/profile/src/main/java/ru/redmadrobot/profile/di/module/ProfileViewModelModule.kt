package ru.redmadrobot.profile.di.module

import dagger.Module
import ru.redmadrobot.common.di.ViewModelFactoryModule

@Module(includes = [ViewModelFactoryModule::class])
abstract class ProfileViewModelModule {
//    @Binds
//    @IntoMap
//    @ViewModelKey(MovieListViewModel::class)
//    internal abstract fun bindAuthViewModel(viewModel: MovieListViewModel): ViewModel
}
