package ru.redmadrobot.profile.di.module

import dagger.Module
import dagger.Provides
import dagger.Reusable
import retrofit2.Retrofit
import ru.redmadrobot.profile.data.ProfileService

@Module
object ProfileServiceModule {
    @Provides
    @Reusable
    fun provideMovieSearchService(retrofit: Retrofit): ProfileService = retrofit.create(ProfileService::class.java)
}
