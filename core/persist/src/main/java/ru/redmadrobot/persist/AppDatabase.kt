package ru.redmadrobot.persist

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.redmadrobot.persist.conventers.LongToCalendarConverter
import ru.redmadrobot.persist.dao.FavoriteMovieDao
import ru.redmadrobot.persist.dao.FavoriteMovieWithGenreDao
import ru.redmadrobot.persist.dao.GenreDao
import ru.redmadrobot.persist.entities.FavoriteMovieDb
import ru.redmadrobot.persist.entities.GenreDb
import ru.redmadrobot.persist.entities.MovieToGenreCrossRef

@Database(
    entities = [
        FavoriteMovieDb::class,
        GenreDb::class,
        MovieToGenreCrossRef::class
    ],
    version = AppDatabase.DB_VERSION,
    exportSchema = true
)
@TypeConverters(LongToCalendarConverter::class)
abstract class AppDatabase : RoomDatabase() {
    companion object {
        const val DB_VERSION = 1
        const val DB_NAME = "movie_app.db"
    }

    abstract fun movieDao(): FavoriteMovieDao
    abstract fun genreDao(): GenreDao
    abstract fun movieWithGenresDao(): FavoriteMovieWithGenreDao
}

