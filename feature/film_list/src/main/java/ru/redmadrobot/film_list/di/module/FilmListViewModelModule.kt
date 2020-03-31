package ru.redmadrobot.film_list.di.module

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import ru.redmadrobot.common.di.ViewModelFactoryModule
import ru.redmadrobot.common.di.ViewModelKey
import ru.redmadrobot.film_list.FilmListViewModel

@Module(includes = [ViewModelFactoryModule::class])
abstract class FilmListViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(FilmListViewModel::class)
    internal abstract fun bindAuthViewModel(viewModel: FilmListViewModel): ViewModel
}
