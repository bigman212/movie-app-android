package ru.redmadrobot.common.extensions

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ProgressBar
import androidx.fragment.app.Fragment

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

fun Fragment.hideKeyboard(focusedView: View?) {
    focusedView?.let { v ->
        val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.hideSoftInputFromWindow(v.windowToken, 0)
    }
}
