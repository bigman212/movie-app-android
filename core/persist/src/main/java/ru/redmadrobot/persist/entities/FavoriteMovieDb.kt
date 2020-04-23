package ru.redmadrobot.persist.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Calendar

@Entity(tableName = FavoriteMovieDb.TABLE_NAME)
data class FavoriteMovieDb(
    @PrimaryKey
    @ColumnInfo(name = COLUMN_ID)
    val movieId: Long,

    @ColumnInfo(name = COLUMN_POSTER_PATH)
    val posterPath: String?,

    @ColumnInfo(name = COLUMN_IS_FOR_ADULTS)
    val isForAdults: Boolean,

    @ColumnInfo(name = COLUMN_OVERVIEW)
    val overview: String,

    @ColumnInfo(name = COLUMN_RELEASE_DATE)
    val releaseDate: Calendar?,

    @ColumnInfo(name = COLUMN_ORIGINAL_TITLE)
    val originalTitle: String,

    @ColumnInfo(name = COLUMN_ORIGINAL_LANGUAGE)
    val originalLanguage: String,

    @ColumnInfo(name = COLUMN_TITLE)
    val title: String,

    @ColumnInfo(name = COLUMN_BACKDROP_PATH)
    val backdropPath: String?,

    @ColumnInfo(name = COLUMN_POPULARITY)
    val popularity: Double,

    @ColumnInfo(name = COLUMN_VOTE_COUNT)
    val voteCount: Int,

    @ColumnInfo(name = COLUMN_IS_VIDEO)
    val isVideo: Boolean,

    @ColumnInfo(name = COLUMN_VOTE_AVERAGE)
    val voteAverage: Double,

    @ColumnInfo(name = COLUMN_RUNTIME)
    val runtime: Int,

    @ColumnInfo(name = COLUMN_IS_WATCHED)
    val isWatched: Boolean = false
) {
    companion object {
        const val TABLE_NAME = "favorite_movies"

        // version 1
        const val COLUMN_ID = "id"
        const val COLUMN_POSTER_PATH = "poster_path"
        const val COLUMN_IS_FOR_ADULTS = "is_for_adults"
        const val COLUMN_OVERVIEW = "overview"
        const val COLUMN_RELEASE_DATE = "release_date"
        const val COLUMN_ORIGINAL_TITLE = "original_title"
        const val COLUMN_ORIGINAL_LANGUAGE = "original_language"
        const val COLUMN_TITLE = "title"
        const val COLUMN_BACKDROP_PATH = "backdrop_path"
        const val COLUMN_POPULARITY = "popularity"
        const val COLUMN_VOTE_COUNT = "vote_count"
        const val COLUMN_IS_VIDEO = "is_video"
        const val COLUMN_VOTE_AVERAGE = "vote_average"
        const val COLUMN_RUNTIME = "runtime"

        // version 2
        const val COLUMN_IS_WATCHED = "is_watched"
    }
}

