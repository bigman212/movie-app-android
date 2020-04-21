package ru.redmadrobot.common.data.movie.entity

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import ru.redmadrobot.common.data.genre.Genre
import ru.redmadrobot.core.network.adapters.AsCalendar
import ru.redmadrobot.persist.entities.FavoriteMovieDb
import ru.redmadrobot.persist.entities.GenreDb
import ru.redmadrobot.persist.junctions.FavoriteMovieWithGenres
import java.util.Calendar

@JsonClass(generateAdapter = true)
data class Movie(
    @field:Json(name = "poster_path")
    val posterPath: String?,

    @field:Json(name = "adult")
    val isForAdults: Boolean,

    @field:Json(name = "overview")
    val overview: String,

    @AsCalendar
    @field:Json(name = "release_date")
    val releaseDate: Calendar?,

    @field:Json(name = "genre_ids")
    val genreIds: List<Long>,

    @Transient
    val genres: List<Genre> = mutableListOf(),

    @field:Json(name = "id")
    val id: Long,

    @field:Json(name = "original_title")
    val originalTitle: String,

    @field:Json(name = "original_language")
    val originalLanguage: String,

    @field:Json(name = "title")
    val title: String,

    @field:Json(name = "backdrop_path")
    val backdropPath: String?,

    @field:Json(name = "popularity")
    val popularity: Double,

    @field:Json(name = "vote_count")
    val voteCount: Int,

    @field:Json(name = "video")
    val isVideo: Boolean,

    @field:Json(name = "vote_average")
    val voteAverage: Double,

    @Transient
    val runtime: Int = 0
) {

    fun toFavoriteMovieDb(): FavoriteMovieDb {
        return FavoriteMovieDb(
            movieId = this.id,
            posterPath = this.posterPath,
            isForAdults = this.isForAdults,
            overview = this.overview,
            releaseDate = this.releaseDate,
            originalTitle = this.originalTitle,
            originalLanguage = this.originalLanguage,
            title = this.title,
            backdropPath = this.backdropPath,
            popularity = this.popularity,
            voteCount = this.voteCount,
            isVideo = this.isVideo,
            voteAverage = this.voteAverage,
            runtime = this.runtime
        )
    }

    companion object {
        fun fromFavoriteMovieDb(favoriteMovieWithGenres: FavoriteMovieWithGenres): Movie {
            val favoriteMovieGenres = favoriteMovieWithGenres.genres.map(Genre.Companion::fromGenreDb)
            val favoriteMovieGenreIds = favoriteMovieWithGenres.genres.map(GenreDb::genreId)
            val movieDb = favoriteMovieWithGenres.favoriteMovie

            return Movie(
                posterPath = movieDb.posterPath,
                isForAdults = movieDb.isForAdults,
                overview = movieDb.overview,
                releaseDate = movieDb.releaseDate,
                genreIds = favoriteMovieGenreIds,
                genres = favoriteMovieGenres,
                id = movieDb.movieId,
                originalTitle = movieDb.originalTitle,
                originalLanguage = movieDb.originalLanguage,
                title = movieDb.title,
                backdropPath = movieDb.backdropPath,
                popularity = movieDb.popularity,
                voteCount = movieDb.voteCount,
                isVideo = movieDb.isVideo,
                voteAverage = movieDb.voteAverage,
                runtime = movieDb.runtime
            )
        }
    }
}
