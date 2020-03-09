package ru.redmadrobot.common.extensions

import android.text.TextUtils
import android.view.View
import android.widget.EditText

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.invisible() {
    this.visibility = View.INVISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}

fun EditText.isEmpty(): Boolean = TextUtils.isEmpty(text.toString())

fun EditText.isNotEmpty(): Boolean = !TextUtils.isEmpty(text.toString())