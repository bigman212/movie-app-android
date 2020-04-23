package ru.redmadrobot.persist

import androidx.room.OnConflictStrategy
import androidx.room.testing.MigrationTestHelper
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import ru.redmadrobot.persist.entities.FavoriteMovieDb
import ru.redmadrobot.persist.migrations.Migration1_2
import ru.redmadrobot.persist.migrations.Migration2_3
import ru.redmadrobot.persist.migrations.Migrations
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class MigrationsTests {

    @get:Rule
    val helper: MigrationTestHelper = MigrationTestHelper(
        InstrumentationRegistry.getInstrumentation(),
        AppDatabase::class.java.canonicalName,
        FrameworkSQLiteOpenHelperFactory()
    )

    @Test
    @Throws(IOException::class)
    fun migration_1_2_is_valid_and_successful() {
        var db = helper.createDatabase(TEST_DB_NAME, Migrations.VERSION_1)
        db = helper.runMigrationsAndValidate(TEST_DB_NAME, Migrations.VERSION_2, true, Migration1_2)
    }

    @Test
    @Throws(IOException::class)
    fun migration_1_2_with_data_is_valid() {
        val favoriteMovieToTest = EntityUtils.createFavoriteMovieDb(customId = 1)
        var db = helper.createDatabase(TEST_DB_NAME, Migrations.VERSION_1)
        db.doAndClose {
            val contentValues = favoriteMovieToTest.asContentValues().withRemoved(FavoriteMovieDb.COLUMN_IS_WATCHED)
            insert(FavoriteMovieDb.TABLE_NAME, OnConflictStrategy.REPLACE, contentValues)
        }
        db = helper.runMigrationsAndValidate(TEST_DB_NAME, Migrations.VERSION_2, true, Migration1_2)

        getMigratedRoomDatabase(helper)
            .movieDao()
            .findById(favoriteMovieToTest.movieId)
            .test()
            .assertNoErrors()
            .assertValue(favoriteMovieToTest)
    }

    @Test
    @Throws(IOException::class)
    fun migration_2_3_is_valid_and_successful() {
        var db = helper.createDatabase(TEST_DB_NAME, Migrations.VERSION_2)
        db = helper.runMigrationsAndValidate(TEST_DB_NAME, Migrations.VERSION_3, true, Migration2_3)
    }

    @Test
    @Throws(IOException::class)
    fun migration_2_3_with_data_is_valid() {
        val favoriteMovieWithAverage = EntityUtils.createFavoriteMovieDb(1).apply {
            copy(voteAverage = 10.0)
        }

        val favoriteMovieWithoutAverage = EntityUtils.createFavoriteMovieDb(2).apply {
            copy(voteAverage = 1.0)
        }

        var db = helper.createDatabase(TEST_DB_NAME, Migrations.VERSION_2)
        db.doAndClose {
            var contentValues = favoriteMovieWithAverage.asContentValues()
                .withRemoved(FavoriteMovieDb.COLUMN_IS_WATCHED)
                .withAdded("is_watched", favoriteMovieWithAverage.isWorthWatched)
            insert(FavoriteMovieDb.TABLE_NAME, OnConflictStrategy.REPLACE, contentValues)

            contentValues = favoriteMovieWithoutAverage.asContentValues()
                .withRemoved(FavoriteMovieDb.COLUMN_IS_WATCHED)
                .withAdded("is_watched", favoriteMovieWithoutAverage.isWorthWatched)
            insert(FavoriteMovieDb.TABLE_NAME, OnConflictStrategy.REPLACE, contentValues)
        }
        db = helper.runMigrationsAndValidate(TEST_DB_NAME, Migrations.VERSION_3, true, Migration2_3)

        val room = getMigratedRoomDatabase(helper)
        room.movieDao()
            .findById(favoriteMovieWithAverage.movieId)
            .test()
            .assertNoErrors()
            .assertValue(favoriteMovieWithAverage)

        room.movieDao()
            .findById(favoriteMovieWithoutAverage.movieId)
            .test()
            .assertNoErrors()
            .assertValue(favoriteMovieWithoutAverage)
    }
}



