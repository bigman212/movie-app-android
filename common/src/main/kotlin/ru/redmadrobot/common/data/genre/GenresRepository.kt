package ru.redmadrobot.common.data.genre

import dagger.Reusable
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import ru.redmadrobot.persist.dao.GenreDao
import ru.redmadrobot.persist.entities.GenreDb
import timber.log.Timber
import javax.inject.Inject

@Reusable
class GenresRepository @Inject constructor(
    private val genresDao: GenreDao,
    private val genreService: GenreApi
) {
    fun fetchGenresAndSave(): Completable {
        return genreService.allMovieGenres()
            .map(GenreResponse::result)
            .flatMapCompletable(this::saveGenres)
    }

    private fun saveGenres(genres: List<Genre>): Completable {
        return Observable.fromIterable(genres)
            .map(Genre::toGenreDb)
            .toList()
            .flatMapCompletable(genresDao::insertAll)
            .doOnComplete { Timber.d("$genres saved!") }
    }

    fun genreById(genreId: Long): Maybe<Genre> { // todo: поправить правило зависимостей
        return genresDao.findById(genreId).map(Genre.Companion::fromGenreDb)
    }

    fun allGenresByIds(genresIds: List<Long>): Single<List<GenreDb>> {
        return genresDao.findAllByIds(genresIds)
            .switchIfEmpty(Single.just(listOf()))
    }
}
