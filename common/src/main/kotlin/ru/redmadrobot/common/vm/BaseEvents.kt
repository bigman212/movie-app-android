package ru.redmadrobot.common.vm

data class MessageEvent(val message: CharSequence) : Event

data class ErrorEvent(val errorMessage: CharSequence) : Event
