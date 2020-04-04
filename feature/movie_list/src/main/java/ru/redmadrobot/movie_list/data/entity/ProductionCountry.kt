package ru.redmadrobot.movie_list.data.entity

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ProductionCountry(
    @field:Json(name = "iso_3166_1")
    val iso3166_1: String,
    @field:Json(name = "name")
    val name: String
)
