package ru.redmadrobot.auth.di.module

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import ru.redmadrobot.auth.login.viewmodel.LoginViewModel
import ru.redmadrobot.common.di.vm.ViewModelFactoryModule
import ru.redmadrobot.common.di.vm.ViewModelKey

@Module(includes = [ViewModelFactoryModule::class])
abstract class AuthViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel::class)
    internal abstract fun bindLoginViewModel(viewModel: LoginViewModel): ViewModel
}
