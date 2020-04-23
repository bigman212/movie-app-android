package ru.redmadrobot.persist.entities

import androidx.room.ColumnInfo
import androidx.room.Entity

/**
 * @see ru.redmadrobot.persist.junctions.FavoriteMovieWithGenres
 * @see ru.redmadrobot.persist.dao.FavoriteMovieWithGenreDao
 */
@Entity(
    tableName = MovieToGenreCrossRef.TABLE_NAME,
    primaryKeys = ["ref_movie_id", "ref_genre_id"]
)
data class MovieToGenreCrossRef(
    @ColumnInfo(name = COLUMN_REF_MOVIE_ID)
    val movieId: Long,
    @ColumnInfo(name = COLUMN_REF_GENRE_ID)
    val genreId: Long
) {
    companion object {
        const val TABLE_NAME = "movie_genre_cross_ref"

        const val COLUMN_REF_MOVIE_ID = "ref_movie_id"
        const val COLUMN_REF_GENRE_ID = "ref_genre_id"
    }
}
