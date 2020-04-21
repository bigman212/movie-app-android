package ru.redmadrobot.common.di

import ru.redmadrobot.core.android.AndroidToolsProvider
import ru.redmadrobot.core.network.di.NetworkProvider
import ru.redmadrobot.persist.di.PersistenceProvider

interface AppProvider : AndroidToolsProvider, NetworkProvider, PersistenceProvider
