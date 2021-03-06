package ru.redmadrobot.common.data.genre

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GenreResponse(
    @field:Json(name = "genres")
    val result: List<Genre>
)
