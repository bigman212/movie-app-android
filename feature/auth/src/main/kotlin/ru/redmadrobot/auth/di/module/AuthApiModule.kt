package ru.redmadrobot.auth.di.module

import dagger.Module
import dagger.Provides
import dagger.Reusable
import retrofit2.Retrofit
import ru.redmadrobot.auth.data.AuthApi

@Module
object AuthApiModule {
    @Provides
    @Reusable
    fun provideAuthApi(retrofit: Retrofit): AuthApi = retrofit.create(AuthApi::class.java)
}
