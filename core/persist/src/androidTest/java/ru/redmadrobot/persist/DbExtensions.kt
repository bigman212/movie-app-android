package ru.redmadrobot.persist

import android.content.ContentValues
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.room.testing.MigrationTestHelper
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import ru.redmadrobot.persist.migrations.Migrations

const val TEST_DB_NAME = "app_db_test.db"

fun SupportSQLiteDatabase.doAndClose(action: SupportSQLiteDatabase.() -> Unit) {
    this.use {
        action.invoke(this)
    }
}

fun ContentValues.withRemoved(key: String): ContentValues {
    remove(key)
    return this
}

fun ContentValues.withAdded(key: String, value: Boolean): ContentValues {
    put(key, value)
    return this
}


fun getMigratedRoomDatabase(
    helper: MigrationTestHelper,
    dbName: String = TEST_DB_NAME,
    vararg migrationsToApply: Migration = Migrations.ALL_MIGRATIONS
): AppDatabase {
    val database = Room.databaseBuilder(
        getApplicationContext(),
        AppDatabase::class.java,
        dbName
    )
        .addMigrations(*migrationsToApply)
        .build()
    // close the database and release any stream resources when the test finishes
    helper.closeWhenFinished(database)
    return database
}
