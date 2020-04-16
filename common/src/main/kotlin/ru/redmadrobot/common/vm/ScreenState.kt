package ru.redmadrobot.common.vm

sealed class ScreenState {
    object Loading : ScreenState()
    object Empty : ScreenState()
    data class Content<T>(val data: T) : ScreenState()
}
