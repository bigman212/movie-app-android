package ru.redmadrobot.common.vm

import androidx.navigation.NavDirections

data class MessageEvent(val message: CharSequence) : Event
data class ErrorEvent(val errorMessage: CharSequence) : Event

interface NavigationEvent : Event

data class NavigateToEvent(val direction: NavDirections) : NavigationEvent
object OnBackPressedEvent : NavigationEvent
object NavigateUpEvent : NavigationEvent
