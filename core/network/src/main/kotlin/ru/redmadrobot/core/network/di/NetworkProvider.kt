package ru.redmadrobot.core.network.di

import retrofit2.Retrofit

interface NetworkProvider {
    fun retrofitClient(): Retrofit
}
