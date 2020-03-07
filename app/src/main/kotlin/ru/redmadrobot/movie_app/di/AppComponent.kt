package ru.redmadrobot.movie_app.di

import dagger.Component
import ru.redmadrobot.core.network.MoviesService
import ru.redmadrobot.core.network.di.AppProvider
import ru.redmadrobot.core.network.di.NetworkProvider
import ru.redmadrobot.core.network.di.component.NetworkComponent
import ru.redmadrobot.movie_app.App
import ru.redmadrobot.movie_app.MainActivity
import javax.inject.Singleton



@Singleton
@Component(
    dependencies = [NetworkProvider::class]
)
interface AppComponent : AppProvider {
    fun inject(activity: MainActivity)
    fun moviesApi(): MoviesService

    class Builder private constructor() {

        companion object {
            fun build(application: App): AppComponent {

                val networkComponent = NetworkComponent.Builder.build("github")
                return DaggerAppComponent.builder()
                    .networkProvider(networkComponent)
                    .build()
            }
        }
    }
}