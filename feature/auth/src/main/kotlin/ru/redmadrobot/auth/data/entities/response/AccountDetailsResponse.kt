package ru.redmadrobot.auth.data.entities.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AccountDetailsResponse(
    @field:Json(name = "avatar")
    val avatar: AccountAvatar,
    @field:Json(name = "id")
    val id: Int,
    @field:Json(name = "iso_639_1")
    val iso639_1: String,
    @field:Json(name = "iso_3166_1")
    val iso3166_1: String,
    @field:Json(name = "name")
    val name: String,
    @field:Json(name = "include_adult")
    val shouldIncludeAdult: Boolean,
    @field:Json(name = "username")
    val username: String
)

@JsonClass(generateAdapter = true)
data class AccountAvatar(
    @field:Json(name = "gravatar")
    val gravatar: Gravatar
)

@JsonClass(generateAdapter = true)
data class Gravatar(
    @field:Json(name = "hash")
    val hash: String
)
