package ru.redmadrobot.core.android

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AndroidToolsModule::class])
interface AndroidToolsComponent : AndroidToolsProvider {
    @Component.Builder
    interface ComponentBuilder {

        @BindsInstance
        fun application(daggerApplication: Application): ComponentBuilder

        fun build(): AndroidToolsComponent
    }

    class Builder private constructor() {

        companion object {
            fun build(daggerApplication: Application): AndroidToolsProvider {
                return DaggerAndroidToolsComponent.builder()
                    .application(daggerApplication)
                    .build()
            }
        }
    }
}