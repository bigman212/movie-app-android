package ru.redmadrobot.common.data.profile

import android.content.SharedPreferences
import androidx.core.content.edit
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import dagger.Reusable
import javax.inject.Inject

@Reusable
class AccountRepository @Inject constructor(
    moshi: Moshi,
    private val sharedPrefs: SharedPreferences
) {
    private val jsonAdapter: JsonAdapter<AccountDetails> = moshi.adapter(AccountDetails::class.java)

    companion object {
        private const val KEY_ACCOUNT = "KEY_ACCOUNT"
    }

    fun save(accountDetails: AccountDetails) {
        sharedPrefs.edit {
            putString(KEY_ACCOUNT, jsonAdapter.toJson(accountDetails))
            apply()
        }
    }

    fun currentAccount(): AccountDetails? {
        val accountAsJson = sharedPrefs.getString(KEY_ACCOUNT, null)
        return accountAsJson?.let(jsonAdapter::fromJson)
    }
}
