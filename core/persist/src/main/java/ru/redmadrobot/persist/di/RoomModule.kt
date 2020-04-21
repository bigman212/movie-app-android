package ru.redmadrobot.persist.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import ru.redmadrobot.persist.AppDatabase
import ru.redmadrobot.persist.dao.FavoriteMovieDao
import ru.redmadrobot.persist.dao.FavoriteMovieWithGenreDao
import ru.redmadrobot.persist.dao.GenreDao
import javax.inject.Singleton

@Module
class RoomModule {
    companion object {
        private const val DB_NAME = "movie_app.db"
    }

    @Singleton
    @Provides
    fun provideRoomDatabase(context: Context): AppDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            DB_NAME
        )
            .build()
    }

    @Singleton
    @Provides
    fun provideMovieDao(database: AppDatabase): FavoriteMovieDao = database.movieDao()

    @Singleton
    @Provides
    fun provideGenreDao(database: AppDatabase): GenreDao = database.genreDao()

    @Singleton
    @Provides
    fun provideMovieWithGenresDao(database: AppDatabase): FavoriteMovieWithGenreDao = database.movieWithGenresDao()
}