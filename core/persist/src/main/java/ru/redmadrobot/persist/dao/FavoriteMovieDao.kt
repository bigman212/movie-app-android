package ru.redmadrobot.persist.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single
import ru.redmadrobot.persist.entities.FavoriteMovieDb
import ru.redmadrobot.persist.entities.FavoriteMovieDb.Companion.COLUMN_ID
import ru.redmadrobot.persist.entities.FavoriteMovieDb.Companion.TABLE_NAME
import ru.redmadrobot.persist.junctions.FavoriteMovieWithGenres

@Dao
interface FavoriteMovieDao {
    @Query("SELECT * FROM $TABLE_NAME")
    fun loadAll(): Single<List<FavoriteMovieDb>>

    @Transaction
    @Query("SELECT * FROM $TABLE_NAME")
    fun loadAllWithGenres(): Single<List<FavoriteMovieWithGenres>>

    @Query("SELECT * FROM $TABLE_NAME WHERE $COLUMN_ID LIKE :movieId LIMIT 1")
    fun findById(movieId: Long): Maybe<FavoriteMovieDb>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg movies: FavoriteMovieDb): Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(movie: FavoriteMovieDb): Completable

    @Delete
    fun delete(movie: FavoriteMovieDb): Completable
}
