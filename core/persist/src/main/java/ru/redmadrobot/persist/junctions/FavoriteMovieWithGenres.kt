package ru.redmadrobot.persist.junctions

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import ru.redmadrobot.persist.entities.FavoriteMovieDb
import ru.redmadrobot.persist.entities.GenreDb
import ru.redmadrobot.persist.entities.MovieToGenreCrossRef

data class FavoriteMovieWithGenres(
    @Embedded
    val favoriteMovie: FavoriteMovieDb,

    @Relation(
        parentColumn = FavoriteMovieDb.COLUMN_ID,
        entity = GenreDb::class,
        entityColumn = GenreDb.COLUMN_ID,
        associateBy = Junction(
            value = MovieToGenreCrossRef::class,
            parentColumn = MovieToGenreCrossRef.COLUMN_REF_MOVIE_ID,
            entityColumn = MovieToGenreCrossRef.COLUMN_REF_GENRE_ID
        )
    )
    val genres: List<GenreDb>
)

