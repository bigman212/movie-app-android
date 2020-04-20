package ru.redmadrobot.auth.view

import android.view.View
import com.xwray.groupie.viewbinding.BindableItem
import ru.redmadrobot.auth.R
import ru.redmadrobot.auth.databinding.ItemPinBackspaceBinding

typealias OnBackspaceClickedListener = () -> Unit

class PinBackspaceItem(private val onBackspaceClicked: OnBackspaceClickedListener) :
    BindableItem<ItemPinBackspaceBinding>() {

    override fun getLayout(): Int = R.layout.item_pin_backspace

    override fun initializeViewBinding(view: View): ItemPinBackspaceBinding = ItemPinBackspaceBinding.bind(view)

    override fun bind(viewBinding: ItemPinBackspaceBinding, position: Int) {
        viewBinding.btnPinBackspace.setOnClickListener {
            onBackspaceClicked.invoke()
        }
    }
}
