package ru.redmadrobot.movie_app.di

import dagger.Component
import ru.redmadrobot.common.di.AppProvider
import ru.redmadrobot.core.network.di.NetworkProvider
import ru.redmadrobot.core.network.di.component.NetworkComponent
import ru.redmadrobot.movie_app.App
import ru.redmadrobot.movie_app.MainActivity
import javax.inject.Singleton


@Singleton
@Component(
    dependencies = [NetworkProvider::class],
    modules = [AppModule::class]
)
interface AppComponent : AppProvider {
    fun inject(activity: MainActivity)

    class Builder private constructor() {

        companion object {
            fun build(application: App, baseUrl: String): AppComponent {
                val networkComponent = NetworkComponent.Builder.build(baseUrl)
                val appModule = AppModule(application)

                return DaggerAppComponent.builder()
                    .appModule(appModule)
                    .networkProvider(networkComponent)
                    .build()
            }
        }
    }
}