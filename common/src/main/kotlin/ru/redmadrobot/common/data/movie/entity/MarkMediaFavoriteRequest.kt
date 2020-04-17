package ru.redmadrobot.common.data.movie.entity

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MarkMediaFavoriteRequest(
    @field:Json(name = "media_id")
    val mediaId: Long,

    @field:Json(name = "media_type")
    val mediaType: MediaType = MediaType.MOVIE,

    @field:Json(name = "favorite")
    val isFavorite: Boolean = true
) {
    enum class MediaType {
        @field:Json(name = "movie")
        MOVIE,

        @field:Json(name = "tv")
        TV_SHOW
    }
}
