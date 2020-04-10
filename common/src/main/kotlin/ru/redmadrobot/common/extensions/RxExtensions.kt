package ru.redmadrobot.common.extensions

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import ru.redmadrobot.core.network.SchedulersProvider

fun <T> Single<T>.uiObserve(): Single<T> {
    return observeOn(AndroidSchedulers.mainThread())
}

fun <T> Single<T>.ioSubscribe(): Single<T> {
    return subscribeOn(Schedulers.io())
}

fun <T : Any> Single<T>.flatMapCompletableAction(action: (T) -> Unit): Completable {
    return flatMapCompletable { param -> Completable.fromAction { action.invoke(param) } }
}

fun <T> Observable<T>.scheduleIoToUi(schedulers: SchedulersProvider): Observable<T> {
    return subscribeOn(schedulers.io()).observeOn(schedulers.ui())
}
