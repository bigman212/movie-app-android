package ru.redmadrobot.auth.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import ru.redmadrobot.auth.viewmodel.AuthViewModel
import ru.redmadrobot.common.di.ViewModelFactory
import ru.redmadrobot.common.di.ViewModelKey

@Module
abstract class AuthViewModelModule {

    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(AuthViewModel::class)
    internal abstract fun bindAuthViewModel(viewModel: AuthViewModel): ViewModel

    //Add more ViewModels here
}