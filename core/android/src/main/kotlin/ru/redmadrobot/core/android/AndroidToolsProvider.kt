package ru.redmadrobot.core.android

import android.content.Context
import android.net.ConnectivityManager

interface AndroidToolsProvider {
    fun context(): Context
    fun networkConnectivityManager(): ConnectivityManager
}