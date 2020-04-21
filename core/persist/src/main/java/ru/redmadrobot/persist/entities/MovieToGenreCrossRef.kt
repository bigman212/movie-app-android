package ru.redmadrobot.persist.entities

import androidx.room.ColumnInfo
import androidx.room.Entity

/**
 * @see ru.redmadrobot.persist.junctions.FavoriteMovieWithGenres
 * @see ru.redmadrobot.persist.dao.FavoriteMovieWithGenreDao
 */
@Entity(
    tableName = "movie_genre_cross_ref",
    primaryKeys = ["ref_movie_id", "ref_genre_id"]
)
data class MovieToGenreCrossRef(
    @ColumnInfo(name = "ref_movie_id")
    val movieId: Long,
    @ColumnInfo(name = "ref_genre_id")
    val genreId: Long
)
