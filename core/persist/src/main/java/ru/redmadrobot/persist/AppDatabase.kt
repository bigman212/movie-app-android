package ru.redmadrobot.persist

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.redmadrobot.persist.conventers.LongToCalendarConverter
import ru.redmadrobot.persist.dao.GenreDao
import ru.redmadrobot.persist.dao.MovieDao
import ru.redmadrobot.persist.dao.MovieWithGenreDao
import ru.redmadrobot.persist.entities.GenreDb
import ru.redmadrobot.persist.entities.MovieDb
import ru.redmadrobot.persist.entities.MovieToGenreCrossRef

@Database(
    entities = [
        MovieDb::class,
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
    }

    abstract fun movieDao(): MovieDao
    abstract fun genreDao(): GenreDao
    abstract fun movieWithGenresDao(): MovieWithGenreDao
}

