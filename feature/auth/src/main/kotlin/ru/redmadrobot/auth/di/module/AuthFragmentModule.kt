package ru.redmadrobot.auth.di.module

import androidx.fragment.app.Fragment
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import ru.redmadrobot.auth.AuthFragment
import ru.redmadrobot.common.di.fragment.FragmentKey

@Module
abstract class AuthFragmentModule {
    @Binds
    @IntoMap
    @FragmentKey(AuthFragment::class)
    internal abstract fun bindAuthFragment(fragment: AuthFragment): Fragment
}
