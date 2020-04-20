package ru.redmadrobot.auth.view

import android.view.View
import com.xwray.groupie.viewbinding.BindableItem
import ru.redmadrobot.auth.R
import ru.redmadrobot.auth.databinding.ItemPinNumberBinding

typealias OnNumberClickedListener = (numberValue: Int) -> Unit

class PinNumberItem(private val number: Int, private val onNumberClicked: OnNumberClickedListener) :
    BindableItem<ItemPinNumberBinding>() {

    override fun getLayout(): Int = R.layout.item_pin_number

    override fun getId(): Long = number.toLong()

    override fun initializeViewBinding(view: View): ItemPinNumberBinding = ItemPinNumberBinding.bind(view)

    override fun bind(viewBinding: ItemPinNumberBinding, position: Int) {
        viewBinding.root.setOnClickListener {
            onNumberClicked.invoke(number)
        }

        viewBinding.btnPinNumber.setOnClickListener {
            onNumberClicked.invoke(number)
        }

        viewBinding.btnPinNumber.text = number.toString()
    }

}
