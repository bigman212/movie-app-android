package ru.redmadrobot.core.network

import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers

class TestSchedulersProvider(
    private val backgroundScheduler: Scheduler = Schedulers.trampoline(),
    private val uiScheduler: Scheduler = Schedulers.trampoline()
) : SchedulersProvider() {
    override fun ui(): Scheduler = uiScheduler
    override fun io(): Scheduler = backgroundScheduler
    override fun computation(): Scheduler = backgroundScheduler
}
