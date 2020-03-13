package ru.redmadrobot.auth.di.module

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import ru.redmadrobot.auth.viewmodel.AuthViewModel
import ru.redmadrobot.common.di.ViewModelFactoryModule
import ru.redmadrobot.common.di.ViewModelKey

@Module(includes = [ViewModelFactoryModule::class])
abstract class AuthViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(AuthViewModel::class)
    internal abstract fun bindAuthViewModel(viewModel: AuthViewModel): ViewModel
}