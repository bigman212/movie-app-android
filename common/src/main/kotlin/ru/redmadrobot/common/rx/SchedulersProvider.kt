package ru.redmadrobot.common.rx

import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

open class SchedulersProvider @Inject constructor() {
    open fun ui(): Scheduler = AndroidSchedulers.mainThread()
    open fun io(): Scheduler = Schedulers.io()
    open fun computation(): Scheduler = Schedulers.computation()
}

fun <T> Single<T>.scheduleIoToUi(schedulers: SchedulersProvider): Single<T> {
    return subscribeOn(schedulers.io()).observeOn(schedulers.ui())
}

fun <T> Single<T>.scheduleComputationToUi(schedulers: SchedulersProvider): Single<T> {
    return subscribeOn(schedulers.computation()).observeOn(schedulers.ui())
}
