package ru.redmadrobot.movie_list.data.entity

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import ru.redmadrobot.common.data.entity.Genre

@JsonClass(generateAdapter = true)
data class MovieDetail(
    @field:Json(name = "adult")
    val isAdult: Boolean,

    @field:Json(name = "backdrop_path")
    val backdropPath: String?,

    @field:Json(name = "belongs_to_collection")
    val belongsToCollection: Any?,

    @field:Json(name = "budget")
    val budget: Int,

    @field:Json(name = "genres")
    val genres: List<Genre>,

    @field:Json(name = "homepage")
    val homepage: String?,

    @field:Json(name = "id")
    val id: Int,

    @field:Json(name = "imdb_id")
    val imdbId: String?,

    @field:Json(name = "original_language")
    val originalLanguage: String,

    @field:Json(name = "original_title")
    val originalTitle: String,

    @field:Json(name = "overview")
    val overview: String?,

    @field:Json(name = "popularity")
    val popularity: Double,

    @field:Json(name = "poster_path")
    val posterPath: String?,

    @field:Json(name = "production_companies")
    val productionCompanies: List<ProductionCompany>,

    @field:Json(name = "production_countries")
    val productionCountries: List<ProductionCountry>,

    @field:Json(name = "release_date")
    val releaseDate: String,

    @field:Json(name = "revenue")
    val revenue: Int,

    @field:Json(name = "runtime")
    val runtime: Int?,

    @field:Json(name = "spoken_languages")
    val spokenLanguages: List<SpokenLanguage>,

    @field:Json(name = "status")
    val status: Status,

    @field:Json(name = "tagline")
    val tagline: String?,

    @field:Json(name = "title")
    val title: String,

    @field:Json(name = "video")
    val hasVideo: Boolean,

    @field:Json(name = "vote_average")
    val voteAverage: Float,

    @field:Json(name = "vote_count")
    val voteCount: Int
) {
    enum class Status {
        Rumored, Planned, In_Production, Post_Production, Released, Canceled
    }
}