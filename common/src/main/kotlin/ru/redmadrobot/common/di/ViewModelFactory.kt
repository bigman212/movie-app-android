package ru.redmadrobot.common.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.MapKey
import javax.inject.Inject
import javax.inject.Provider
import kotlin.reflect.KClass

typealias ViewModelClassKey = Class<out ViewModel>
typealias ViewModelProviderByName = Provider<ViewModel>

class ViewModelFactory @Inject constructor(
    private val providers: Map<ViewModelClassKey, @JvmSuppressWildcards ViewModelProviderByName>
) : ViewModelProvider.Factory {

    @Throws(RuntimeException::class)
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val provider = providers[modelClass] // не нашли VM в списке всех VM по ключу - имени класса
            ?: findViewModelByClass(modelClass)
            ?: error("Unknown ViewModel class $modelClass")
        return try {
            provider.get() as T
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }

    private fun <T : ViewModel> findViewModelByClass(modelClassToInit: Class<T>): @JvmSuppressWildcards ViewModelProviderByName? {
        return providers.asIterable()
            .find { modelClassToInit.isAssignableFrom(it.key) }
            ?.value
    }
}

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@MapKey
annotation class ViewModelKey(val value: KClass<out ViewModel>)

