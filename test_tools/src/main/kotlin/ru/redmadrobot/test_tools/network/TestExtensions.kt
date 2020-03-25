package ru.redmadrobot.test_tools.network

import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import okhttp3.mockwebserver.MockResponse
import javax.net.ssl.HttpsURLConnection

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

fun MockResponse.success(body: String = ""): MockResponse {
    return setResponseCode(HttpsURLConnection.HTTP_OK).setBody(body)
}

fun MockResponse.badRequest(body: String = ""): MockResponse {
    return setResponseCode(HttpsURLConnection.HTTP_BAD_REQUEST).setBody(body)
}

fun MockResponse.notFound(): MockResponse {
    return setResponseCode(HttpsURLConnection.HTTP_NOT_FOUND)
}
