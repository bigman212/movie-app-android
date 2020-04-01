package ru.redmadrobot.common.extensions

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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
            this@delegate.value = value
        }

        override fun getValue(thisRef: Any, property: KProperty<*>): T {
            return requireValue()
        }
    }
}

private fun <T : Any> LiveData<T>.requireValue(): T = checkNotNull(value)
