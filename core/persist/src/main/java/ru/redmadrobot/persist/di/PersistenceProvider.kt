package ru.redmadrobot.persist.di

import ru.redmadrobot.persist.dao.FavoriteMovieDao
import ru.redmadrobot.persist.dao.FavoriteMovieWithGenreDao
import ru.redmadrobot.persist.dao.GenreDao

interface PersistenceProvider {
    fun movieDao(): FavoriteMovieDao
    fun genreDao(): GenreDao
    fun movieWithGenresDao(): FavoriteMovieWithGenreDao
}
