package ru.redmadrobot.common.di

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import dagger.MapKey
import javax.inject.Provider
import kotlin.reflect.KClass

class InjectFragmentFactory(
    private val providers: Map<String, Provider<Fragment>>
) : FragmentFactory() {

    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
        return providers[className]?.get() ?: super.instantiate(classLoader, className)
    }

    companion object {

        operator fun invoke(
            providers: Map<Class<out Fragment>, Provider<Fragment>>
        ): InjectFragmentFactory {
            return InjectFragmentFactory(
                providers.mapKeys { (fragmentClass, _) -> fragmentClass.name }
            )
        }
    }
}

@MapKey
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER)
@Retention(AnnotationRetention.RUNTIME)
annotation class FragmentKey(val value: KClass<out Fragment>)
