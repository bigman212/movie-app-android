package ru.redmadrobot.common.data.movie.entity

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import ru.redmadrobot.common.data.genre.Genre
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@JsonClass(generateAdapter = true)
data class Movie(
    @field:Json(name = "poster_path")
    val posterPath: String?,

    @field:Json(name = "adult")
    val isForAdults: Boolean,

    @field:Json(name = "overview")
    val overview: String,

    @field:Json(name = "release_date")
    internal val releaseDateAsStr: String,

    @field:Json(name = "genre_ids")
    val genreIds: List<Int>,

    @Transient
    val genres: List<Genre> = mutableListOf(),

    @field:Json(name = "id")
    val id: Int,

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
    val releaseDate: Calendar = parseReleaseDate()

    private fun parseReleaseDate(): Calendar {
        val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val date = format.parse(releaseDateAsStr) ?: Date()

        return Calendar.getInstance().apply {
            time = date
        }
    }
}
