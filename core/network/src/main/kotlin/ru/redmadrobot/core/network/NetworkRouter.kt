package ru.redmadrobot.core.network

/**
 * Класс содержащий константы - URL для спецификации [MoviesService]
 */
object NetworkRouter {
    const val BASE_URL = "https://api.themoviedb.org"

    private const val api_number = "/3"

    private const val auth = "$api_number/authentication"
    const val AUTH_TOKEN_NEW = "$auth/token/new"
    const val AUTH_VALIDATE_TOKEN = "$auth/token/validate_with_login"
    const val AUTH_CREATE_SESSION_ID = "$auth/session/new"

    private const val tv = "$api_number/tv"
    const val TV_POPULAR = "$tv/popular"

    private const val movie = "$api_number/movie"
    const val MOVIES_POPULAR = "$movie/popular"
}
