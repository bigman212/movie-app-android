package ru.redmadrobot.common.di

import ru.redmadrobot.core.android.AndroidToolsProvider
import ru.redmadrobot.core.network.di.NetworkProvider

interface AppProvider : AndroidToolsProvider, NetworkProvider {
//    нужно для реализации инъекции фрагментов
//    fun fragmentFactory(): FragmentFactory
}

