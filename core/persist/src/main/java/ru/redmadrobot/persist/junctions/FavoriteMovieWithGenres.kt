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
        parentColumn = "movie_id",
        entity = GenreDb::class,
        entityColumn = "genre_id",
        associateBy = Junction(
            value = MovieToGenreCrossRef::class,
            parentColumn = "ref_movie_id",
            entityColumn = "ref_genre_id"
        )
    )
    val genres: List<GenreDb>
)

