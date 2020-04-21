package ru.redmadrobot.persist.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import io.reactivex.Single
import ru.redmadrobot.persist.entities.MovieToGenreCrossRef
import ru.redmadrobot.persist.junctions.MovieWithGenres

@Dao
interface MovieWithGenreDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(movieWithGenres: MovieToGenreCrossRef)

    @Transaction
    @Query("SELECT * FROM movies")
    fun loadAll(): Single<List<MovieWithGenres>>
}
