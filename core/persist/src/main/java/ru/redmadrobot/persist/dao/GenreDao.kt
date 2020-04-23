package ru.redmadrobot.persist.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single
import ru.redmadrobot.persist.entities.GenreDb
import ru.redmadrobot.persist.entities.GenreDb.Companion.COLUMN_ID
import ru.redmadrobot.persist.entities.GenreDb.Companion.TABLE_NAME

@Dao
interface GenreDao {
    @Query("SELECT * FROM $TABLE_NAME")
    fun loadAll(): Single<List<GenreDb>>

    @Query("SELECT * FROM $TABLE_NAME WHERE $COLUMN_ID LIKE :genreId LIMIT 1")
    fun findById(genreId: Long): Maybe<GenreDb>

    @Query("SELECT * FROM $TABLE_NAME WHERE $COLUMN_ID IN (:genreIds)")
    fun findAllByIds(genreIds: List<Long>): Maybe<List<GenreDb>>

    @Insert
    fun insertAll(movies: List<GenreDb>): Completable

    @Delete
    fun delete(movie: GenreDb): Completable
}
