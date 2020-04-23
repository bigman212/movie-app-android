package ru.redmadrobot.persist.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import ru.redmadrobot.persist.AppDatabase
import ru.redmadrobot.persist.dao.FavoriteMovieDao
import ru.redmadrobot.persist.dao.FavoriteMovieWithGenreDao
import ru.redmadrobot.persist.dao.GenreDao
import ru.redmadrobot.persist.migrations.Migrations
import javax.inject.Singleton

@Module
class RoomModule {

    @Singleton
    @Provides
    fun provideRoomDatabase(context: Context): AppDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            AppDatabase.DB_NAME
        )
            .addMigrations(*Migrations.ALL_MIGRATIONS)
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
