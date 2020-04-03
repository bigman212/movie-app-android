package ru.redmadrobot.core.network

import android.annotation.SuppressLint
import android.content.SharedPreferences
import javax.inject.Inject

class SessionIdRepository @Inject constructor(private val sharedPrefs: SharedPreferences) {
    companion object {
        private const val PREFS_SESSION_ID_KEY = "session_id_key"
    }

    @SuppressLint("NewApi")
    fun saveSessionId(sessionId: String) =
        sharedPrefs.edit()
            .putString(PREFS_SESSION_ID_KEY, sessionId)
            .apply()

    fun getSessionId(): String? = sharedPrefs.getString(PREFS_SESSION_ID_KEY, null)

    fun sessionIdExists(): Boolean = sharedPrefs.contains(PREFS_SESSION_ID_KEY)
}
