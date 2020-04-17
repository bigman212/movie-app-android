package ru.redmadrobot.common.data.profile

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AccountDetails(
    @field:Json(name = "account_id")
    val id: Long,
    @field:Json(name = "account_name")
    val name: String,
    @field:Json(name = "account_is_adult")
    val isAdult: Boolean,
    @field:Json(name = "account_username")
    val username: String,
    @field:Json(name = "account_avatar_hash")
    val avatar: String // gravatar hash
)
