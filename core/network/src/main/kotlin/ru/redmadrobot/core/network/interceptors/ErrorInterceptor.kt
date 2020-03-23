package ru.redmadrobot.core.network.interceptors

import android.annotation.SuppressLint
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import okhttp3.Interceptor
import okhttp3.Response
import ru.redmadrobot.core.network.NetworkErrorHandler
import javax.inject.Inject

class ErrorInterceptor @Inject constructor(
    private val errorHandler: NetworkErrorHandler,
    private val connectivityManager: ConnectivityManager
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        if (noNetworkConnection()) {
            throw errorHandler.noNetworkInternetException()
        }
        val request = chain.request()
        val response = chain.proceed(request)
        if (!response.isSuccessful) {
            throw errorHandler.networkExceptionToThrow(response)
        }
        return response
    }

    @SuppressLint("MissingPermission")
    private fun noNetworkConnection(): Boolean {
        val netCap = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        return netCap?.let { !netCap.hasCellularTransport() and !netCap.hasWiFiTransport() } ?: false
    }

    private fun NetworkCapabilities.hasCellularTransport() = hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
    private fun NetworkCapabilities.hasWiFiTransport() = hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
}
