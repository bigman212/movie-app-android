package ru.redmadrobot.movie_app

object Versions {
    const val appId = "ru.redmadrobot.movie_app"
    const val compileSdk = 29
    const val buildTools = "29.0.2"

    const val minSdk = 21
    const val targetSdk = 29

    const val versionCode = 1
    const val versionName = "0.0.1"
}

object TestDependencies {
    // Не нужно выносить версии наружу, все зависимости подлючаются двумя переменными
    private const val junit5_version = "5.6.0"
    private const val junit4_version = "4.13"

    private const val mockito_core_version = "2.18.0"
    private const val mockito_kotlin_version = "2.2.0"

    private const val spek_version = "2.0.3"

    private const val mockwebserver_version = "4.4.0"

    private const val assertj_version = "3.15.0"

    val testImpl = listOf(
        // junit5
        "org.junit.jupiter:junit-jupiter-api:$junit5_version",
        "org.junit.jupiter:junit-jupiter-params:$junit5_version",
        "org.junit.platform:junit-platform-runner:1.6.1",
        "junit:junit:$junit4_version",

        // mockito
        "org.mockito:mockito-core:$mockito_core_version",
        "com.nhaarman.mockitokotlin2:mockito-kotlin:$mockito_kotlin_version",

        // spek
        "org.spekframework.spek2:spek-dsl-jvm:$spek_version",
        "org.spekframework.spek2:spek-runner-junit5:$spek_version",

        "com.squareup.okhttp3:mockwebserver:$mockwebserver_version",

        "org.assertj:assertj-core:$assertj_version",

        "org.jetbrains.kotlin:kotlin-reflect:${Dependencies.kotlin_version}",
        "org.jetbrains.kotlin:kotlin-test:${Dependencies.kotlin_version}"
    )

    val testRuntime = listOf(
        "org.junit.jupiter:junit-jupiter-engine:$junit5_version",
        "org.junit.vintage:junit-vintage-engine:$junit5_version",
        // spek JUnit test engine
        "org.spekframework.spek2:spek-runner-junit5:$spek_version"
    )

    private const val junit5_android_version = "1.0.0"
    private const val test_runner_version = "1.0.2"
    private const val espresso_core_version = "3.0.2"

    val androidTestImpl = listOf(
        "de.mannodermaus.junit5:android-test-core:$junit5_android_version"
    )

    val androidTestRuntime = listOf(
        "de.mannodermaus.junit5:android-test-runner:$junit5_android_version"
    )

    val androidTest = listOf(
        "com.android.support.test:runner:$test_runner_version",
        "com.android.support.test.espresso:espresso-core:$espresso_core_version"
    )
}

object Dependencies {
    internal const val kotlin_version = "1.3.70"
    private const val lifecycle_version = "2.2.0"
    private const val navigation_version = "2.2.0"
    private const val material_version = "1.1.0"

    private const val constraint_layout_version = "1.1.3"
    private const val android_ktx_version = "1.2.0"

    private const val dagger_version = "2.26"

    private const val rxjava_version = "2.2.18"
    private const val rxandroid_version = "2.1.1"

    private const val moshi_version = "1.9.2"
    private const val retrofit_version = "2.7.1"
    private const val okhttp_logging_version = "4.4.0"

    private const val timber_version = "4.7.1"

    const val Kotlin = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlin_version"

    object Lifecycle {
        const val ViewModel = "androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycle_version"
        const val Extensions = "androidx.lifecycle:lifecycle-extensions:$lifecycle_version"
    }

    object Navigation {
        const val Fragment = "androidx.navigation:navigation-fragment-ktx:$navigation_version"
        const val UI_KTX = "androidx.navigation:navigation-ui-ktx:$navigation_version"
    }

    const val Material = "com.google.android.material:material:$material_version"

    object AndroidKtx {
        const val Core = "androidx.core:core-ktx:$android_ktx_version"
    }

    const val Constraint_layout = "androidx.constraintlayout:constraintlayout:$constraint_layout_version"

    object Dagger {
        const val Dagger = "com.google.dagger:dagger:$dagger_version"
        const val Processor = "com.google.dagger:dagger-compiler:$dagger_version"
    }

    val RxJava = listOf(
        "io.reactivex.rxjava2:rxjava:$rxjava_version",
        "io.reactivex.rxjava2:rxandroid:$rxandroid_version"
    )

    object Moshi {
        const val Moshi = "com.squareup.moshi:moshi:$moshi_version"
        const val KotlinCodeGen = "com.squareup.moshi:moshi-kotlin-codegen:$moshi_version"
    }

    val Retrofit = listOf(
        "com.squareup.retrofit2:retrofit:$retrofit_version",
        "com.squareup.retrofit2:converter-gson:$retrofit_version",
        "com.squareup.retrofit2:adapter-rxjava2:$retrofit_version",
        "com.squareup.retrofit2:converter-moshi:$retrofit_version",

        "com.squareup.okhttp3:logging-interceptor:$okhttp_logging_version"
    )

    object Tools {
        const val Timber = "com.jakewharton.timber:timber:$timber_version"
    }
}
