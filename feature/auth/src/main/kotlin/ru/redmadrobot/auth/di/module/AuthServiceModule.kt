package ru.redmadrobot.auth.di.module

import dagger.Module
import dagger.Provides
import dagger.Reusable
import retrofit2.Retrofit
import ru.redmadrobot.auth.data.AuthService

@Module
object AuthServiceModule {
    @Provides
    @Reusable
    fun provideAuthService(retrofit: Retrofit): AuthService = retrofit.create(AuthService::class.java)
}
