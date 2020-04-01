package ru.redmadrobot.movie_list.di.module

import androidx.fragment.app.Fragment
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import ru.redmadrobot.common.di.FragmentKey
import ru.redmadrobot.movie_list.search.MovieListSearchFragment

@Module
interface MovieListSearchFragmentModule {
    @Binds
    @IntoMap
    @FragmentKey(MovieListSearchFragment::class)
    fun bindMessageFragmentToFragmentForMultiBinding(fragment: MovieListSearchFragment): Fragment
}
