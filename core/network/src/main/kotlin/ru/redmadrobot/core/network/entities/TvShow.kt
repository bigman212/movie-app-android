package ru.redmadrobot.core.network.entities

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TvShow(
    @field:Json(name = "original_name") val originalName: String,
    @field:Json(name = "genre_ids") val genreIds: List<Int>,
    @field:Json(name = "name") val name: String,
    @field:Json(name = "origin_country") val originCountries: List<String>,
    @field:Json(name = "vote_count") val voteCount: Int,
    @field:Json(name = "first_air_date") val firstAirDate: String,
    @field:Json(name = "id") val id: Int,
    @field:Json(name = "overview") val overview: String
)