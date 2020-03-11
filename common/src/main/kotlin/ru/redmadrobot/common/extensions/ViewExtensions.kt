package ru.redmadrobot.common.extensions

import android.view.View
import android.widget.ProgressBar

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.invisible() {
    this.visibility = View.INVISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}

fun View.isVisible(visible: Boolean) = if (visible) visible() else invisible()

fun ProgressBar.showLoading(loading: Boolean) = if (loading) visible() else gone()