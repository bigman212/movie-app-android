package ru.redmadrobot.common

import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers
import ru.redmadrobot.common.rx.SchedulersProvider

class TestSchedulersProvider(
    private val backgroundScheduler: Scheduler = Schedulers.trampoline(),
    private val uiScheduler: Scheduler = Schedulers.trampoline()
) : SchedulersProvider() {
    override fun ui(): Scheduler = uiScheduler
    override fun io(): Scheduler = backgroundScheduler
    override fun computation(): Scheduler = backgroundScheduler
}
