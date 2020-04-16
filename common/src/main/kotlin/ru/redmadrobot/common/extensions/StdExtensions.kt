package ru.redmadrobot.common.extensions

import java.util.Calendar

fun Calendar.day(): Int = get(Calendar.DAY_OF_MONTH)

fun Calendar.month(): Int = get(Calendar.MONTH)

fun Calendar.year(): Int = get(Calendar.YEAR)
