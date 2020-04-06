package ru.redmadrobot.movie_app.di

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import ru.redmadrobot.common.di.vm.ViewModelFactoryModule
import ru.redmadrobot.common.di.vm.ViewModelKey
import ru.redmadrobot.movie_app.MainViewModel

@Module(includes = [ViewModelFactoryModule::class])
abstract class MainViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    internal abstract fun bindAuthViewModel(viewModel: MainViewModel): ViewModel
}
