package ru.redmadrobot.persist.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import io.reactivex.Completable
import io.reactivex.Single
import ru.redmadrobot.persist.entities.FavoriteMovieDb
import ru.redmadrobot.persist.entities.MovieToGenreCrossRef
import ru.redmadrobot.persist.junctions.FavoriteMovieWithGenres

@Dao
interface FavoriteMovieWithGenreDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(movieWithGenres: MovieToGenreCrossRef): Completable

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(moviesWithGenres: List<MovieToGenreCrossRef>): Completable

    @Transaction
    @Query("SELECT * FROM ${FavoriteMovieDb.TABLE_NAME}")
    fun loadAll(): Single<List<FavoriteMovieWithGenres>>
}
