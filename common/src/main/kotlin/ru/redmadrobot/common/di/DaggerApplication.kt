package ru.redmadrobot.common.di

import android.content.Context

interface DaggerApplication {
    fun getApplicationContext(): Context
    fun getApplicationProvider(): AppProvider
}
