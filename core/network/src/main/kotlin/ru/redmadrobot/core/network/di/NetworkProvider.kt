package ru.redmadrobot.core.network.di

import com.squareup.moshi.Moshi
import retrofit2.Retrofit

interface NetworkProvider {
    fun retrofitClient(): Retrofit
    fun moshi(): Moshi
}
