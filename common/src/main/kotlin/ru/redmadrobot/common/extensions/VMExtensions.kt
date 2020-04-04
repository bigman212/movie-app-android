package ru.redmadrobot.common.extensions

import android.os.Looper
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.Transformations
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * Слушаем изменения у ViewState в VM и меняем значение value в LiveData
 *
 * val liveState: MutableLiveData<ProfileViewState> = MutableLiveData(createInitialState())
 * private var state: ProfileViewState by liveState.delegate()
 *
 * state = state.isFetching() // liveData меняет свое состояние на новый state
 *
 */
fun <T : Any> MutableLiveData<T>.delegate(): ReadWriteProperty<Any, T> {
    return object : ReadWriteProperty<Any, T> {
        override fun setValue(thisRef: Any, property: KProperty<*>, value: T) {
            if (Thread.currentThread() == Looper.getMainLooper().thread) {
                this@delegate.value = value // или postValue? :hmm:
            } else {
                this@delegate.postValue(value)
            }
        }

        override fun getValue(thisRef: Any, property: KProperty<*>): T {
            return requireValue()
        }
    }
}

private fun <T : Any> LiveData<T>.requireValue(): T = checkNotNull(value)

fun <T> LiveData<T>.observe(owner: LifecycleOwner, observer: (T) -> Unit) {
    observe(owner, Observer {
        observer.invoke(it)
    })
}

private fun <X, Y> LiveData<X>.map(transform: (X) -> Y): LiveData<Y> {
    return Transformations.map(this, transform)
}

private fun <T> LiveData<T>.distinctUntilChanged(): LiveData<T> {
    return Transformations.distinctUntilChanged(this)
}

fun <X, Y> LiveData<X>.mapDistinct(transform: (X) -> Y): LiveData<Y> {
    return map(transform)
        .distinctUntilChanged()
}
