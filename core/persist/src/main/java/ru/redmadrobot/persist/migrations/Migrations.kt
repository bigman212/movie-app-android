package ru.redmadrobot.persist.migrations

object Migrations {
    const val VERSION_1 = 1
    const val VERSION_2 = 2
    const val VERSION_3 = 3
    const val VERSION_4 = 4

    val ALL_MIGRATIONS = arrayOf(Migration1_2, Migration2_3)
}






