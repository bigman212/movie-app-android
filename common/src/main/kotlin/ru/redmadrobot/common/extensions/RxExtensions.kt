package ru.redmadrobot.common.extensions

import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

fun <T> Single<T>.uiObserve(): Single<T> {
    return observeOn(AndroidSchedulers.mainThread())
}

fun <T> Single<T>.ioSubscribe(): Single<T> {
    return subscribeOn(Schedulers.io())
}
