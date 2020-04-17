package ru.redmadrobot.common.data.movie.entity

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import ru.redmadrobot.common.data.genre.Genre
import ru.redmadrobot.core.network.adapters.AsCalendar
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
)
