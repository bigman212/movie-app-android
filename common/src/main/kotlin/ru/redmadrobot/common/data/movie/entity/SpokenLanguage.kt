package ru.redmadrobot.common.data.movie.entity

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SpokenLanguage(
    @field:Json(name = "iso_639_1")
    val iso639_1: String,
    @field:Json(name = "name")
    val name: String
)
