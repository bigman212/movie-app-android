package ru.redmadrobot.common.di

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import dagger.Module
import dagger.Provides
import javax.inject.Provider

@Module
object InjectFragmentFactoryModule {
    @Provides
    fun provideFragmentFactory(
        fragmentProviders: Map<Class<out Fragment>, @JvmSuppressWildcards Provider<Fragment>>
    ): FragmentFactory {
        return InjectFragmentFactory(fragmentProviders)
    }
}
