package ru.redmadrobot.profile.di.module

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import ru.redmadrobot.common.di.ViewModelFactoryModule
import ru.redmadrobot.common.di.ViewModelKey
import ru.redmadrobot.profile.ProfileViewModel

@Module(includes = [ViewModelFactoryModule::class])
abstract class ProfileViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(ProfileViewModel::class)
    internal abstract fun bindAuthViewModel(viewModel: ProfileViewModel): ViewModel
}
