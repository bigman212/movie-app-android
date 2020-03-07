package ru.redmadrobot.core.network

import io.reactivex.Observable
import retrofit2.http.GET
import ru.redmadrobot.core.network.entities.AuthResponse

interface MoviesService {
    @GET("/")
    fun login(): Observable<AuthResponse>
}