package ru.redmadrobot.persist.di

import ru.redmadrobot.persist.dao.GenreDao
import ru.redmadrobot.persist.dao.MovieDao

interface PersistenceProvider {
    fun movieDao(): MovieDao
    fun genreDao(): GenreDao
}
