package ru.redmadrobot.profile.di.module

import dagger.Module
import dagger.Provides
import dagger.Reusable
import retrofit2.Retrofit
import ru.redmadrobot.profile.data.ProfileApi

@Module
object ProfileApiModule {
    @Provides
    @Reusable
    fun provideMovieSearchService(retrofit: Retrofit): ProfileApi = retrofit.create(ProfileApi::class.java)
}
