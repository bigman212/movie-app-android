package ru.redmadrobot.movie_app.di

import android.app.Application
import dagger.Component
import ru.redmadrobot.common.di.AppProvider
import ru.redmadrobot.core.android.AndroidToolsComponent
import ru.redmadrobot.core.android.AndroidToolsProvider
import ru.redmadrobot.core.network.di.NetworkProvider
import ru.redmadrobot.core.network.di.component.NetworkComponent
import javax.inject.Singleton

@Singleton
@Component(
    dependencies = [NetworkProvider::class, AndroidToolsProvider::class]
)
interface AppComponent : AppProvider {

    @Component.Factory
    interface Factory {
        fun create(
            androidToolsProvider: AndroidToolsProvider,
            networkProvider: NetworkProvider
        ): AppComponent
    }

    companion object {
        fun init(application: Application): AppComponent {

            val androidToolsProvider = AndroidToolsComponent.Builder.build(application)
            val networkProvider = NetworkComponent.Builder.build(androidToolsProvider)

            return DaggerAppComponent.factory()
                .create(androidToolsProvider, networkProvider)
        }
    }
}