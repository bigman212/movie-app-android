package ru.redmadrobot.auth.view

import android.view.View
import androidx.core.view.isInvisible
import com.xwray.groupie.viewbinding.BindableItem
import ru.redmadrobot.auth.R
import ru.redmadrobot.auth.databinding.ItemPinExitBinding

typealias OnExitClickedListener = () -> Unit

class PinExitItem(private val visibility: Boolean, private val onExitClicked: OnExitClickedListener) :
    BindableItem<ItemPinExitBinding>() {

    override fun getLayout(): Int = R.layout.item_pin_exit

    override fun initializeViewBinding(view: View): ItemPinExitBinding = ItemPinExitBinding.bind(view)

    override fun bind(viewBinding: ItemPinExitBinding, position: Int) {
        viewBinding.root.isInvisible = !visibility
        viewBinding.tvPinExitTitle.setOnClickListener {
            onExitClicked.invoke()
        }
    }
}
