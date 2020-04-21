package ru.redmadrobot.persist.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import ru.redmadrobot.persist.entities.GenreDb

@Dao
interface GenreDao {
    @Query("SELECT * FROM genres")
    fun loadAll(): List<GenreDb>

    @Query("SELECT * FROM genres WHERE genre_id LIKE :genreId LIMIT 1")
    fun findById(genreId: Long): GenreDb

    @Insert
    fun insertAll(vararg movies: GenreDb)

    @Delete
    fun delete(movie: GenreDb)
}
