package com.efremov.santa.core.widget

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Typeface
import android.util.AttributeSet
import android.util.Log
import android.widget.TextView
import com.efremov.santa.R
import com.efremov.santa.core.utils.FontManager

class CustomFontTextView : TextView {

    constructor(context: Context) : this(context, null) {
    }

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0) {
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        if (isInEditMode) return

        val typedArray: TypedArray = context!!.obtainStyledAttributes(attrs,
            R.styleable.CustomFontTextView
        )
        val fontAsset = typedArray.getString(R.styleable.CustomFontTextView_typeFaceAsset)
        if (fontAsset != null) {
            if (fontAsset.isNotEmpty()) {
                val tf = FontManager.getFont(fontAsset)
                var style = Typeface.NORMAL
                val size = textSize
                if (typeface != null) {
                    style = typeface.style
                }
                if (tf != null) {
                    setTypeface(tf, style)
                } else {
                    Log.d("FontText", String.format("Could not create a font from asset: %s", fontAsset))
                }
            }
        }

        typedArray.recycle()

    }
}