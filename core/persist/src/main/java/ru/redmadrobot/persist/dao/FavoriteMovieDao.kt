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
import ru.redmadrobot.persist.junctions.FavoriteMovieWithGenres

@Dao
interface FavoriteMovieDao {
    @Query("SELECT * FROM favorite_movies")
    fun loadAll(): Single<List<FavoriteMovieDb>>

    @Transaction
    @Query("SELECT * FROM favorite_movies")
    fun loadAllWithGenres(): Single<List<FavoriteMovieWithGenres>>

    @Query("SELECT * FROM favorite_movies WHERE id LIKE :movieId LIMIT 1")
    fun findById(movieId: Long): Maybe<FavoriteMovieDb>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg movies: FavoriteMovieDb): Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(movie: FavoriteMovieDb): Completable

    @Delete
    fun delete(movie: FavoriteMovieDb): Completable
}
