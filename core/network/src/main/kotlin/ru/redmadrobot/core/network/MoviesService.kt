package ru.redmadrobot.core.network

import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.POST
import ru.redmadrobot.core.network.entities.AuthRequest
import ru.redmadrobot.core.network.entities.AuthResponse

interface MoviesService {
    @POST("/")
    fun login(@Body body: AuthRequest): Observable<AuthResponse>
}