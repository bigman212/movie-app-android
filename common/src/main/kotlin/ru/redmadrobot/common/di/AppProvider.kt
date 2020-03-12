package ru.redmadrobot.common.di

import android.content.Context
import ru.redmadrobot.core.network.di.NetworkProvider

interface AppProvider : NetworkProvider {
    fun context(): Context
}

