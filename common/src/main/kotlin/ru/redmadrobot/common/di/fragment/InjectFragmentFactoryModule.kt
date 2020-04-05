package ru.redmadrobot.common.di.fragment

import androidx.fragment.app.FragmentFactory
import dagger.Binds
import dagger.Module

@Module
interface InjectFragmentFactoryModule {
    @Binds
    fun bindFragmentFactory(factory: InjectFragmentFactory): FragmentFactory
}
