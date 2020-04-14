package ru.redmadrobot.common.data.movie.entity

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MarkMovieFavoriteRequest(
    @field:Json(name = "media_id")
    val mediaId: Int,

    @field:Json(name = "media_type")
    val mediaType: String = "movie",

    @field:Json(name = "favorite")
    val isFavorite: Boolean = true
)
