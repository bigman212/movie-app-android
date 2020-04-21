package ru.redmadrobot.persist.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import io.reactivex.Completable
import io.reactivex.Single
import ru.redmadrobot.persist.entities.MovieDb
import ru.redmadrobot.persist.junctions.MovieWithGenres

@Dao
interface MovieDao {
    @Query("SELECT * FROM movies")
    fun loadAll(): Single<List<MovieDb>>

    @Transaction
    @Query("SELECT * FROM movies")
    fun loadAllWithGenres(): Single<List<MovieWithGenres>>

    @Query("SELECT * FROM movies WHERE movie_id LIKE :movieId LIMIT 1")
    fun findById(movieId: Long): MovieDb

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg movies: MovieDb): Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(movie: MovieDb): Completable

    @Delete
    fun delete(movie: MovieDb): Completable
}
