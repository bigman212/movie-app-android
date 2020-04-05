package ru.redmadrobot.common.di.genre

import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import ru.redmadrobot.common.data.GenreService

@Module
object GenreServiceModule {
    @Provides
    fun provideGenreService(retrofit: Retrofit): GenreService = retrofit.create(GenreService::class.java)
}
