package ru.redmadrobot.persist

import android.content.ContentValues
import ru.redmadrobot.persist.entities.FavoriteMovieDb
import java.util.Calendar

object EntityUtils {
    private var id = 0L

    fun createFavoriteMovieDb(customId: Long = id): FavoriteMovieDb {
        return FavoriteMovieDb(
            movieId = customId,
            posterPath = "poster_path",
            isForAdults = false,
            overview = "overview",
            releaseDate = Calendar.getInstance(),
            originalTitle = "originalTitle",
            originalLanguage = "originalLanguage",
            title = "title-$id",
            backdropPath = "backdrop_path",
            popularity = 1.1,
            voteCount = 100,
            isVideo = false,
            voteAverage = 120.1,
            runtime = 100
        ).also { id++ }
    }
}

fun FavoriteMovieDb.asContentValues(): ContentValues {
    return ContentValues().apply {
        put(FavoriteMovieDb.COLUMN_ID, movieId)
        put(FavoriteMovieDb.COLUMN_POSTER_PATH, posterPath)
        put(FavoriteMovieDb.COLUMN_IS_FOR_ADULTS, isForAdults)
        put(FavoriteMovieDb.COLUMN_OVERVIEW, overview)
        put(FavoriteMovieDb.COLUMN_RELEASE_DATE, releaseDate?.timeInMillis)
        put(FavoriteMovieDb.COLUMN_ORIGINAL_TITLE, originalTitle)
        put(FavoriteMovieDb.COLUMN_ORIGINAL_LANGUAGE, originalLanguage)
        put(FavoriteMovieDb.COLUMN_TITLE, title)
        put(FavoriteMovieDb.COLUMN_BACKDROP_PATH, backdropPath)
        put(FavoriteMovieDb.COLUMN_POPULARITY, popularity)
        put(FavoriteMovieDb.COLUMN_VOTE_COUNT, voteCount)
        put(FavoriteMovieDb.COLUMN_IS_VIDEO, isVideo)
        put(FavoriteMovieDb.COLUMN_VOTE_AVERAGE, voteAverage)
        put(FavoriteMovieDb.COLUMN_RUNTIME, runtime)
        put(FavoriteMovieDb.COLUMN_IS_WATCHED, isWorthWatched)
    }
}
