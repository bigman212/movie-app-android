package ru.redmadrobot.auth.pin

import ru.redmadrobot.auth.R
import ru.redmadrobot.auth.pin.data.PinValueRepository
import ru.redmadrobot.common.base.BaseViewModel
import ru.redmadrobot.common.vm.Event
import ru.redmadrobot.common.vm.MessageEvent
import ru.redmadrobot.common.vm.NavigateToEvent
import timber.log.Timber
import javax.inject.Inject

class PinCreateViewModel @Inject constructor(
    private val pinRepo: PinValueRepository
) : BaseViewModel() {

    private var pinEnteredFirstTime = true
    private var firstPinValues = mutableListOf<Int>()

    object PinsAreNotEqualEvent : Event
    object ReEnterPin : Event

    fun onPinFullyEntered(values: List<Int>) {
        if (pinEnteredFirstTime) {
            firstPinValues.addAll(values)
            events.offer(ReEnterPin)
            pinEnteredFirstTime = false
        } else if (!pinEnteredFirstTime && enteredPinsAreEqual(values)) {
            savePin(values)
            firstPinValues.clear()
        } else {
            events.offer(PinsAreNotEqualEvent)
            pinEnteredFirstTime = true
            firstPinValues.clear()
        }
    }

    private fun savePin(values: List<Int>) {
        val valuesAsString = values.map(Int::toString)
        pinRepo.savePinValue(valuesAsString)
            .subscribe(
                {
                    events.offer(MessageEvent(stringId = R.string.pin_pin_created))
                    events.offer(NavigateToEvent(PinCreateFragmentDirections.toMovieListMainFragment()))
                },
                { error ->
                    Timber.e(error)
                    offerErrorEvent(error)
                })
            .disposeOnCleared()
    }

    private fun enteredPinsAreEqual(repeatedPin: List<Int>): Boolean = firstPinValues == repeatedPin
}



