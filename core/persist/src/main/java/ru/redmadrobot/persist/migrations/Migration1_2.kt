package ru.redmadrobot.persist.migrations

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import ru.redmadrobot.persist.migrations.Migrations.VERSION_1
import ru.redmadrobot.persist.migrations.Migrations.VERSION_2

object Migration1_2 : Migration(VERSION_1, VERSION_2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL(
            "ALTER TABLE favorite_movies ADD COLUMN is_watched INTEGER NOT NULL DEFAULT 0"
        )
    }
}
