package ru.redmadrobot.common.base

import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import javax.inject.Inject

open class BaseViewModel @Inject constructor() : ViewModel() {

    protected val compositeDisposable: CompositeDisposable by lazy { CompositeDisposable() }

    fun Disposable.disposeOnCleared() {
        compositeDisposable.add(this)
    }

    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }
}

