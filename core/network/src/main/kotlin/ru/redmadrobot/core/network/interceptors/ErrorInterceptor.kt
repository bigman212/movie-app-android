package ru.redmadrobot.core.network.interceptors

import android.accounts.NetworkErrorException
import android.annotation.SuppressLint
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.net.ssl.HttpsURLConnection

class NoInternetConnectionException : NetworkErrorException()

class NetworkErrorInterceptor @Inject constructor() : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        if (!hasNetworkConnection()) {
            throw NoInternetConnectionException()
        }
        val request = chain.request()
        val response = chain.proceed(request)
        when (response.code) {
            HttpsURLConnection.HTTP_BAD_REQUEST -> {

            }
        }
        return response
    }

    @SuppressLint("MissingPermission")
    private fun hasNetworkConnection(): Boolean {
        return true
    }
}