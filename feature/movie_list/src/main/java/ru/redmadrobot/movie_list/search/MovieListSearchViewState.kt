package ru.redmadrobot.movie_list.search

import ru.redmadrobot.movie_list.Movie

data class MovieListSearchViewState(
    val isFetching: Boolean = false,

    val movies: List<Movie> = listOf(),

    val errorMessage: String? = null
) {
    fun fetchingState() = copy(isFetching = true, errorMessage = null)
    fun moviesFoundState(moviesFound: List<Movie>) = copy(movies = moviesFound, isFetching = false)

    fun noMoviesFoundState() = copy(movies = listOf(), isFetching = false)

    fun errorState(error: String?) = copy(errorMessage = error, isFetching = false)
}
