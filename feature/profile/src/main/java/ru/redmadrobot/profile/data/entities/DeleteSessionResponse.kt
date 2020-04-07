package ru.redmadrobot.profile.data.entities

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class DeleteSessionResponse(
    @field:Json(name = "success")
    val sessionIsDeleted: Boolean
)
