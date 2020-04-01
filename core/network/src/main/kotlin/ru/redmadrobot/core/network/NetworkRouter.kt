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

    const val AUTH_DELETE_SESSION = "$auth/session"

    private const val tv = "$api_number/tv"
    const val TV_POPULAR = "$tv/popular"

    private const val movie = "$api_number/movie"
    const val MOVIES_POPULAR = "$movie/popular"

    private const val search = "$api_number/search"
    const val SEARCH_MOVIE = "$search/movie"

    const val IMAGES = "https://image.tmdb.org/t/p/w500"
}
