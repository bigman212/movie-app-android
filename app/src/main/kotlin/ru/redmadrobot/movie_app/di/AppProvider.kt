package ru.redmadrobot.movie_app.di


import android.content.Context
import ru.redmadrobot.core.network.di.NetworkProvider
import ru.redmadrobot.movie_app.App

interface AppProvider : NetworkProvider {
    fun context(): Context
    fun application(): App
}

