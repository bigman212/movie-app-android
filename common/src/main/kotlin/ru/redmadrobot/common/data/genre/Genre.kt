package ru.redmadrobot.common.data.genre

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Genre(
    @field:Json(name = "id")
    val id: Int,

    @field:Json(name = "name")
    val name: String
)

