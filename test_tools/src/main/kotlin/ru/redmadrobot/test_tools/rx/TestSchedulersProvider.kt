package ru.redmadrobot.test_tools.rx

import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers
import ru.redmadrobot.common.rx.SchedulersProvider
import ru.redmadrobot.core.network.SchedulersProvider

class TestSchedulersProvider(
    private val backgroundScheduler: Scheduler = Schedulers.trampoline(),
    private val uiScheduler: Scheduler = Schedulers.trampoline()
) : ru.redmadrobot.core.network.SchedulersProvider() {
    override fun ui(): Scheduler = uiScheduler
    override fun io(): Scheduler = backgroundScheduler
    override fun computation(): Scheduler = backgroundScheduler
}
