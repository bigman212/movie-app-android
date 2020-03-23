package ru.redmadrobot.core.network

import android.content.Context
import com.squareup.moshi.Moshi
import okhttp3.Response
import okhttp3.ResponseBody
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton
import javax.net.ssl.HttpsURLConnection
import kotlin.reflect.KClass

@Singleton
class NetworkErrorHandler @Inject constructor(context: Context, val moshi: Moshi) {

    object ErrorStatusCode {
        const val INVALID_API_KEY = 7
        const val INVALID_CREDENTIALS = 30
        const val INVALID_REQUEST_TOKEN = 33 // истек срок годности request_token

        const val SESSION_DENIED = 17 // истек срок годности session_id

        const val UNKNOWN_ERROR = -1 // неизвестная ошибка
    }

    private val contextString: (resId: Int) -> String = context::getString

    private val unknownErrorResponse
        get() = ErrorResponse(
            statusCode = ErrorStatusCode.UNKNOWN_ERROR,
            statusMessage = statusCodeToContextString(ErrorStatusCode.UNKNOWN_ERROR)
        )

    fun noNetworkInternetException() = NetworkException.NoNetworkConnection()

    fun networkExceptionToThrow(responseWithError: Response): NetworkException {
        val error = parseErrorBody(responseWithError.bodyCopy())

        return when (responseWithError.code) {
            HttpsURLConnection.HTTP_BAD_REQUEST -> NetworkException.BadRequest(error)
            HttpsURLConnection.HTTP_UNAUTHORIZED -> NetworkException.Unauthorized(error)
            else -> NetworkException.Unknown(error)
        }
    }

    private fun parseErrorBody(body: ResponseBody?): ErrorResponse {
        return if (body != null) {
            val bodyAsJson = body.string()
            try {
                val rawErrorFromServer = moshi.fromJsonOrThrow(ErrorResponse::class, bodyAsJson)
                return rawErrorFromServer.toErrorWithContextString()
            } catch (nonValidJsonOrNull: IOException) {
                Timber.e(nonValidJsonOrNull)
                unknownErrorResponse
            }
        } else {
            unknownErrorResponse
        }
    }

    private fun statusCodeToContextString(statusCode: Int): String {
        return when (statusCode) {
            ErrorStatusCode.INVALID_API_KEY -> contextString(R.string.network_error_invalid_api_key)
            ErrorStatusCode.INVALID_CREDENTIALS -> contextString(R.string.network_invalid_credentials)
            ErrorStatusCode.INVALID_REQUEST_TOKEN -> contextString(R.string.network_error_invalid_api_key)
            ErrorStatusCode.SESSION_DENIED -> contextString(R.string.network_error_session_denied)
            else -> contextString(R.string.network_error_unknown)
        }
    }

    private fun ErrorResponse.toErrorWithContextString() = copy(statusMessage = statusCodeToContextString(statusCode))

    private fun Response.bodyCopy(): ResponseBody? = body?.let { peekBody(it.contentLength()) }

    @Throws(IOException::class)
    private fun <T : Any> Moshi.fromJsonOrThrow(type: KClass<T>, content: String): T =
        adapter(type.java).fromJson(content)
            ?: throw IOException("Moshi adapter($type) with data ($content) returned null")
}
