package ru.redmadrobot.auth.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.viewbinding.BindableItem
import ru.redmadrobot.auth.databinding.ViewPinKeyboardBinding

class PinKeyboardView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    companion object {
        private const val PIN_NUMBER_ITEMS_COUNT = 9
        private const val PIN_REQUIRED_INPUTS_COUNT = 4
        private const val KEYBOARD_PIN_NUMBER_COLUMNS = 3
    }

    private var isExitEnabled: Boolean = false
    private var numberClickListener: ((Int) -> Unit)? = null
    private var backspaceClickListener: (() -> Unit)? = null
    private var exitClickListener: (() -> Unit)? = null
    private var inputsFilledListener: (() -> Unit)? = null

    private val enteredPinNumbers: MutableList<Int> = mutableListOf()

    private var indicatorsItems = MutableList(PIN_REQUIRED_INPUTS_COUNT) {
        PinIndicatorItem(PinIndicatorItem.Mode.EMPTY, it.toLong())
    }

    private val groupAdapter = GroupAdapter<GroupieViewHolder>().apply {
        spanCount = KEYBOARD_PIN_NUMBER_COLUMNS
    }

    private val groupAdapterIndicators = GroupAdapter<GroupieViewHolder>()

    init {
        val layoutInflater = LayoutInflater.from(context)
        val viewBinding = ViewPinKeyboardBinding.inflate(layoutInflater, this, true)

        initRecyclerViews(viewBinding)
    }

    fun setOnInputFilledListener(callback: () -> Unit) {
        inputsFilledListener = callback
    }

    fun setOnNumberClickedListener(callback: (Int) -> Unit) {
        numberClickListener = callback
    }

    fun setOnExitClickListener(callback: () -> Unit) {
        exitClickListener = callback
    }

    fun setOnBackspaceClickListener(callback: () -> Unit) {
        backspaceClickListener = callback
    }

    private fun initRecyclerViews(viewBinding: ViewPinKeyboardBinding) {
        viewBinding.rvPinIndicators.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = groupAdapterIndicators
        }

        viewBinding.rvPinKeyboard.apply {
            layoutManager = GridLayoutManager(context, groupAdapter.spanCount)
            adapter = groupAdapter
        }
        groupAdapterIndicators.update(indicatorsItems)
        groupAdapter.update(genKeyboardItems())
    }

    private fun genKeyboardItems(): List<BindableItem<*>> {
        return MutableList<BindableItem<*>>(PIN_NUMBER_ITEMS_COUNT) {
            PinNumberItem(it + 1, ::onNumberClicked)
        }
            .also {
                it += PinExitItem(visibility = isExitEnabled, onExitClicked = ::onExitClicked)
                it += PinNumberItem(0, ::onNumberClicked)
                it += PinBackspaceItem(::onBackspaceClicked)
            }
    }

    private fun updateIndicatorItems(withError: Boolean = false) {
        (0 until PIN_REQUIRED_INPUTS_COUNT).forEach { i ->
            val indicator = indicatorsItems[i]

            if (withError) {
                indicatorsItems[i] = indicator.changeModeTo(PinIndicatorItem.Mode.ERROR)
            } else if (i < enteredPinNumbers.size) {
                indicatorsItems[i] = indicator.changeModeTo(PinIndicatorItem.Mode.FILLED)
            } else {
                indicatorsItems[i] = indicator.changeModeTo(PinIndicatorItem.Mode.EMPTY)
            }
        }
        groupAdapterIndicators.update(indicatorsItems)
    }

    private fun onNumberClicked(number: Int) {
        if (enteredPinNumbers.size < PIN_REQUIRED_INPUTS_COUNT) {
            enteredPinNumbers += number
            updateIndicatorItems()
            numberClickListener?.invoke(number)
        } else if (enteredPinNumbers.size == PIN_REQUIRED_INPUTS_COUNT) {
            inputsFilledListener?.invoke()
        }
    }

    private fun onExitClicked() {
        exitClickListener?.invoke()
    }

    private fun onBackspaceClicked() {
        if (enteredPinNumbers.isNotEmpty()) {
            enteredPinNumbers.removeAt(enteredPinNumbers.lastIndex)
            updateIndicatorItems()
        }
        backspaceClickListener?.invoke()
    }
}
