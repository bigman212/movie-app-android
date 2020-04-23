package ru.redmadrobot.persist.migrations

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import ru.redmadrobot.persist.entities.FavoriteMovieDb

object Migration2_3 : Migration(
    Migrations.VERSION_2,
    Migrations.VERSION_3
) {

    //    Нужно сделать миграцию с пересозданием таблицы с целью замены колонки isWatched: Boolean,
//    на колонку isWorthWatching: Boolean (без дефолтного значения) - заполнить ее значением isWatched.
//    Также протестировать миграцию схемы.
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL(
            """
            CREATE TABLE IF NOT EXISTS `${FavoriteMovieDb.TABLE_NAME}_3`
            (`id` INTEGER NOT NULL, 
            `poster_path` TEXT, 
            `is_for_adults` INTEGER NOT NULL, 
            `overview` TEXT NOT NULL, 
            `release_date` INTEGER, 
            `original_title` TEXT NOT NULL, 
            `original_language` TEXT NOT NULL,
            `title` TEXT NOT NULL,
            `backdrop_path` TEXT,
            `popularity` REAL NOT NULL,
            `vote_count` INTEGER NOT NULL,
            `is_video` INTEGER NOT NULL,
            `vote_average` REAL NOT NULL,
            `runtime` INTEGER NOT NULL,
            `is_worth_watching` INTEGER NOT NULL,
            PRIMARY KEY(`id`)
            )
        """.trimIndent()
        )

        database.execSQL(
            """INSERT INTO ${FavoriteMovieDb.TABLE_NAME}_3 (
                id,
                poster_path,
                is_for_adults,
                overview,
                release_date,
                original_title,
                original_language,
                title,
                backdrop_path,
                popularity,
                vote_count,
                is_video,
                vote_average,
                runtime,
                is_worth_watching
                ) SELECT id, 
                poster_path,
                is_for_adults,
                overview,
                release_date,
                original_title,
                original_language,
                title,
                backdrop_path,
                popularity,
                vote_count,
                is_video,
                vote_average,
                runtime,
                vote_average > 7
                FROM ${FavoriteMovieDb.TABLE_NAME}""".trimIndent()
        )

        database.execSQL("DROP TABLE ${FavoriteMovieDb.TABLE_NAME}")

        database.execSQL("ALTER TABLE ${FavoriteMovieDb.TABLE_NAME}_3 RENAME TO ${FavoriteMovieDb.TABLE_NAME}")
    }
}
