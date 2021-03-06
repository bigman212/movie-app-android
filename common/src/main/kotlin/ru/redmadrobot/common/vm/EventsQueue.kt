package ru.redmadrobot.common.vm

import android.app.Activity
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import ru.redmadrobot.common.extensions.observe
import java.util.LinkedList
import java.util.Queue

interface Event

class EventsQueue : MutableLiveData<Queue<Event>>() {
    fun offer(event: Event) {
        val queue = (value ?: LinkedList()).apply { add(event) }
        value = queue
    }
}

fun Fragment.observeEvents(eventsQueue: EventsQueue, eventHandler: (Event) -> Unit) {
    observe(eventsQueue) { queue: Queue<Event> ->
        while (queue.isNotEmpty()) {
            eventHandler(queue.remove())
        }
    }
}

fun Activity.observeEvents(eventsQueue: EventsQueue, eventHandler: (Event) -> Unit) {
    eventsQueue.observe(this as LifecycleOwner, Observer { queue: Queue<Event> ->
        while (queue.isNotEmpty()) {
            eventHandler(queue.remove())
        }
    })
}



