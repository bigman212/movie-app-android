package ru.redmadrobot.core.network

import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response

fun OkHttpClient.makePost(url: String, json: String): Response {
    val body: RequestBody = json.toRequestBody("application/json; charset=utf-8".toMediaType())
    val request: Request = Request.Builder()
        .url(url)
        .post(body)
        .build()
    val response: Response = newCall(request).execute()
    return response
}

fun OkHttpClient.makeGet(url: String): Response {
    val request: Request = Request.Builder()
        .url(url)
        .get()
        .build()
    val response: Response = newCall(request).execute()
    return response
}
