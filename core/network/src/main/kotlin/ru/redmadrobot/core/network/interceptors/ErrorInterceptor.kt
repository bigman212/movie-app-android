package ru.redmadrobot.core.network.interceptors

import android.annotation.SuppressLint
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.squareup.moshi.Moshi
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.ResponseBody
import ru.redmadrobot.core.network.entities.ErrorResponse
import ru.redmadrobot.core.network.entities.HttpException
import javax.inject.Inject
import javax.net.ssl.HttpsURLConnection

class NetworkErrorInterceptor @Inject constructor(
    private val moshi: Moshi,
    private val connectivityManager: ConnectivityManager
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        if (noNetworkConnection()) {
            throw HttpException.NoNetworkConnection()
        }
        val request = chain.request()
        val response = chain.proceed(request)
        when (response.code) {
            HttpsURLConnection.HTTP_BAD_REQUEST -> {
                response.body?.let {
                    val peekBody = response.peekBody(it.contentLength())
                    val err = parseErrorBody(peekBody)
                    throw HttpException.BadRequest(err)
                }
            }
            HttpsURLConnection.HTTP_UNAUTHORIZED -> {
                response.body?.let {
                    val peekBody = response.peekBody(it.contentLength())
                    val err = parseErrorBody(peekBody)
                    throw HttpException.Unauthorized(err)
                }
            }
        }
        return response
    }

    @SuppressLint("MissingPermission", "NewApi")
    private fun noNetworkConnection(): Boolean {
        val netCap = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        return netCap?.let { !netCap.hasCellularTransport() and !netCap.hasWiFiTransport() } ?: false
    }

    @SuppressLint("InlinedApi")
    fun NetworkCapabilities.hasCellularTransport() = hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)

    @SuppressLint("InlinedApi")
    fun NetworkCapabilities.hasWiFiTransport() = hasTransport(NetworkCapabilities.TRANSPORT_WIFI)

    private fun parseErrorBody(body: ResponseBody): ErrorResponse {
        return moshi.adapter(ErrorResponse::class.java).fromJson(body.string())!!
    }
}