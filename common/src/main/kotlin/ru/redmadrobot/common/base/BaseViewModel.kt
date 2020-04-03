package ru.redmadrobot.common.base

import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import ru.redmadrobot.common.vm.ErrorEvent
import ru.redmadrobot.common.vm.EventsQueue
import javax.inject.Inject

open class BaseViewModel @Inject constructor() : ViewModel() {

    protected val compositeDisposable: CompositeDisposable by lazy { CompositeDisposable() }

    val events: EventsQueue by lazy { EventsQueue() }

    fun Disposable.disposeOnCleared() {
        compositeDisposable.add(this)
    }

    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }

    protected fun offerErrorEvent(error: Throwable) {
        events.offer(ErrorEvent(error.message ?: error.toString()))
    }
}

