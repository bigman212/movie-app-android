package ru.redmadrobot.core.network

import okhttp3.mockwebserver.MockResponse
import ru.redmadrobot.core.network.NetworkErrorHandler.ErrorStatusCode
import javax.net.ssl.HttpsURLConnection

internal object MockErrorResponseCreator {
    internal fun fromUrl(url: String, responseCode: Int = HttpsURLConnection.HTTP_BAD_REQUEST): MockResponse =
        if (urlCanBeMocked(url)) {
            MockResponse()
                .setResponseCode(responseCode)
                .setBody(urlToErrorJson.getValue(url))
        } else {
            MockResponse()
                .setResponseCode(HttpsURLConnection.HTTP_NOT_FOUND)
        }

    private val urlToErrorJson = mapOf(
        NetworkRouter.AUTH_TOKEN_NEW to errorJson(
            ErrorStatusCode.INVALID_API_KEY,
            "invalid_api_key"
        ),
        NetworkRouter.AUTH_CREATE_SESSION_ID to errorJson(
            ErrorStatusCode.INVALID_REQUEST_TOKEN,
            "invalid_request_token"
        ),
        NetworkRouter.AUTH_VALIDATE_TOKEN to errorJson(
            ErrorStatusCode.INVALID_CREDENTIALS,
            "invalid credentials"
        )
    )

    private fun urlCanBeMocked(url: String): Boolean {
        return url in urlToErrorJson.keys
    }

    private fun errorJson(code: Int, message: String): String {
        //language=JSON
        return "{\"status_code\": $code,\"status_message\": \"$message\"}"
    }
}
