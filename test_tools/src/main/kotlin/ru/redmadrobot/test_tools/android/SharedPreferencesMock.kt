package ru.redmadrobot.test_tools.android

import android.content.SharedPreferences
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.mock
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.ArgumentMatchers.anyString

class SharedPreferencesMock(keyToValue: Map<String, String> = mapOf()) {
    private val editor = mock<SharedPreferences.Editor> {
        on { putString(anyString(), anyString()) }.doReturn(it)
        on { putInt(anyString(), anyInt()) }.doReturn(it)
    }

    val sharedPrefs = mock<SharedPreferences> {
        for ((key, value) in keyToValue) {
            on { getString(eq(key), any()) } doReturn (value)
        }

        on { edit() } doReturn (editor)
    }
}
