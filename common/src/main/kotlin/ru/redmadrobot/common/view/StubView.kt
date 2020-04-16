package ru.redmadrobot.common.view

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.annotation.DrawableRes
import androidx.core.view.isGone
import kotlinx.android.synthetic.main.view_stub.view.*
import ru.redmadrobot.common.R

class StubView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    FrameLayout(context, attrs, defStyleAttr) {

    @DrawableRes
    var stubImage: Int = R.drawable.ic_movie_list_background_girl
        set(value) {
            field = value
            view_stub_image.setImageResource(field)
        }

    var stubTitle: String = ""
        set(value) {
            field = value
            view_stub_text_view_title.text = field
        }

    private var addButtonCaption: String? = null
        set(value) {
            field = value
            view_stub_button_add.isGone = value == null
            if (value != null) view_stub_button_add.text = field
        }

    /***/
    fun setOnButtonClickListener(listener: () -> Unit) {
        view_stub_button_add.setOnClickListener { listener() }
    }

    init {
        LayoutInflater.from(context).inflate(R.layout.view_stub, this, true)
        lateinit var attrsArray: TypedArray
        try {
            attrsArray = context.obtainStyledAttributes(attrs, R.styleable.StubView)
            with(attrsArray) {
                stubTitle = getString(R.styleable.StubView_stubTitle) ?: ""
                stubImage = getResourceId(R.styleable.StubView_stubIcon, -1)
                addButtonCaption = getString(R.styleable.StubView_addButtonCaption)
            }
        } finally {
            attrsArray.recycle()
        }
    }
}
