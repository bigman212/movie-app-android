package ru.redmadrobot.common.extensions

import android.os.Looper
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.distinctUntilChanged
import androidx.lifecycle.map
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

inline fun <T> Fragment.observe(liveData: LiveData<T>, crossinline block: (T) -> Unit) {
    liveData.observe(viewLifecycleOwner, Observer { block.invoke(it) })
}

fun <X, Y> LiveData<X>.mapDistinct(transform: (X) -> Y): LiveData<Y> {
    return map(transform)
        .distinctUntilChanged()
}
