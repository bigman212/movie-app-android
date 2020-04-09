package ru.redmadrobot.movie_list.search

import ru.redmadrobot.common.adapters.Movie

data class MovieListSearchViewState(
    val isFetching: Boolean = false,

    val movies: List<Movie> = listOf()
) {
    fun fetchingState() = copy(isFetching = true)
    fun fetchingFinishedState() = copy(isFetching = false)
}
