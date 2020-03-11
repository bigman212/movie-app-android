package ru.redmadrobot.movie_app.di

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import ru.redmadrobot.common.di.AppProvider
import ru.redmadrobot.core.network.di.NetworkProvider
import ru.redmadrobot.movie_app.MainActivity
import javax.inject.Singleton

@Singleton
@Component(
    dependencies = [NetworkProvider::class],
    modules = [AppModule::class]
)
interface AppComponent : AppProvider {
    fun inject(activity: MainActivity)

    @Component.Builder
    interface Builder {
        fun build(): AppComponent

        @BindsInstance
        fun application(application: Application): Builder
        fun networkProvider(networkProvider: NetworkProvider): Builder
    }
}