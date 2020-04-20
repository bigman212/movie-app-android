package ru.redmadrobot.auth.pin.data

import android.content.SharedPreferences
import androidx.core.content.edit
import io.reactivex.Completable
import timber.log.Timber
import javax.inject.Inject

class PinValueRepository @Inject constructor(private val sharedPrefs: SharedPreferences) {
    companion object {
        private const val PREFS_PIN_KEY = "prefs_pin_key"
    }

    fun savePinValue(values: List<String>): Completable {
        return Completable.fromCallable {
            sharedPrefs.edit {
                putString(PREFS_PIN_KEY, values.joinToString())
                apply()

                Timber.d("$values are saved as pin")

                return@fromCallable Completable.complete()
            }
        }
    }

    fun checkPin(values: List<String>): Boolean {
        val pinValues = allPinValues()
        return pinValues.containsAll(values)
    }

    fun deletePin() {
        sharedPrefs.edit {
            remove(PREFS_PIN_KEY)
            apply()
        }
    }

    private fun allPinValues(): List<String> {
        val pinValue = sharedPrefs.getString(PREFS_PIN_KEY, null)
        if (!pinValue.isNullOrBlank()) {
            return pinValue.split(',')
        }
        return listOf()
    }
}
