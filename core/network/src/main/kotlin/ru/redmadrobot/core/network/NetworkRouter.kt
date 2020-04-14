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
    const val MOVIE_DETAILS = "$movie/{movie_id}"

    private const val search = "$api_number/search"
    const val SEARCH_MOVIE = "$search/movie"

    private const val genre = "$api_number/genre"
    const val GENRE_MOVIE_ALL = "$genre/movie/list"

    private const val account = "$api_number/account"
    const val ACCOUNT_DETAILS = account

    private const val favorite = "$account/{account_id}/favorite"
    const val FAVORITE = "$favorite/"
    const val FAVORITE_MOVIES = "$favorite/movies"

    const val IMAGES = "https://image.tmdb.org/t/p/w500"
}
