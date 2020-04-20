package ru.redmadrobot.auth.view

import android.content.res.ColorStateList
import android.view.View
import com.xwray.groupie.viewbinding.BindableItem
import ru.redmadrobot.auth.R
import ru.redmadrobot.auth.databinding.ItemPinIndicatorBinding
import ru.redmadrobot.common.extensions.context
import ru.redmadrobot.common.extensions.getCompatColor

class PinIndicatorItem(private var viewMode: Mode, private val indicatorId: Long) :
    BindableItem<ItemPinIndicatorBinding>() {
    enum class Mode {
        EMPTY,
        FILLED,
        ERROR
    }

    override fun getId(): Long = indicatorId

    override fun getLayout(): Int = R.layout.item_pin_indicator

    override fun initializeViewBinding(view: View): ItemPinIndicatorBinding = ItemPinIndicatorBinding.bind(view)

    override fun bind(viewBinding: ItemPinIndicatorBinding, position: Int) {
        val indicatorColor = when (viewMode) {
            Mode.EMPTY -> viewBinding.context.getCompatColor(R.color.pin_indicator_empty)
            Mode.FILLED -> viewBinding.context.getCompatColor(R.color.pin_indicator_filled)
            Mode.ERROR -> viewBinding.context.getCompatColor(R.color.pin_indicator_error)
        }
        viewBinding.imgPinIndicator.imageTintList = ColorStateList.valueOf(indicatorColor)
    }

    fun changeModeTo(viewMode: Mode): PinIndicatorItem {
        return PinIndicatorItem(viewMode, indicatorId)
    }
}

