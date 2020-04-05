package ru.redmadrobot.common.di

import androidx.fragment.app.FragmentFactory
import androidx.lifecycle.ViewModelProvider
import ru.redmadrobot.common.di.vm.ViewModelFactoryModule
import ru.redmadrobot.core.android.AndroidToolsProvider
import ru.redmadrobot.core.network.di.NetworkProvider

interface AppProvider :
    AndroidToolsProvider,
    NetworkProvider {

    fun fragmentFactory(): FragmentFactory

    /**
     * @see [ViewModelFactoryModule]
     */
    fun viewModelFactory(): ViewModelProvider.Factory
}
