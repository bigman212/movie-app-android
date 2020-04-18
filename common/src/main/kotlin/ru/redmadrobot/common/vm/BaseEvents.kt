package ru.redmadrobot.common.vm

import androidx.annotation.StringRes
import androidx.navigation.NavDirections

/**
 * Используется для показа информационных сообщений пользователю
 * Можно передать строку, можно id строки из strings.xml
 */
data class MessageEvent(val message: CharSequence? = null, @StringRes val stringId: Int? = null) : Event
data class ErrorEvent(val errorMessage: CharSequence) : Event

interface NavigationEvent : Event

data class NavigateToEvent(val direction: NavDirections) : NavigationEvent
object OnBackPressedEvent : NavigationEvent
object NavigateUpEvent : NavigationEvent
