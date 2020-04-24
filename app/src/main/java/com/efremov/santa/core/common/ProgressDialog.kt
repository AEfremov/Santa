package com.efremov.santa.core.common

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import android.widget.ImageView
import androidx.fragment.app.DialogFragment
import com.efremov.santa.R
import kotlinx.android.synthetic.main.fragment_progress.*

class ProgressDialog : DialogFragment()/*(context, R.style.ProgressDialogTheme)*/ {

    private var image: ImageView? = null

//    init {
//        val rotateAnimation = RotateAnimation(
//            0f,
//            360f,
//            Animation.RELATIVE_TO_SELF,
//            0.5f,
//            Animation.RELATIVE_TO_SELF,
//            0.5f
//        )
//
//        rotateAnimation.duration = 600
//        rotateAnimation.repeatCount = Animation.INFINITE
//        rotateAnimation.interpolator = LinearInterpolator()
//
//        val wlmp = window.attributes
//        wlmp.gravity = Gravity.CENTER_HORIZONTAL
//        window.attributes = wlmp
//        setTitle(null)
//        setCancelable(false)
//        setOnCancelListener(null)
//        val layout = LinearLayout(context)
//        layout.isClickable = true
//        layout.isFocusable = true
//        layout.isFocusableInTouchMode = true
//        layout.orientation = LinearLayout.VERTICAL
//        val params = LinearLayout.LayoutParams(
//            LinearLayout.LayoutParams.MATCH_PARENT,
//            LinearLayout.LayoutParams.MATCH_PARENT)
//
//        image = ImageView(context)
////        val imageParams = LinearLayout.LayoutParams(200, 200)
////        image?.layoutParams = imageParams
////        image?.setPadding(20, 20, 20, 20)
//        image?.setImageResource(R.drawable.ic_gingerbread_man)
//        layout.addView(image, params)
//
//        image?.animation = rotateAnimation
//
//        setContentView(layout, params)
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_FRAME, R.style.ProgressDialogTheme)
        isCancelable = false
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        LayoutInflater.from(context).inflate(R.layout.fragment_progress, null)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val rotateAnimation = RotateAnimation(
            0f,
            360f,
            Animation.RELATIVE_TO_SELF,
            0.5f,
            Animation.RELATIVE_TO_SELF,
            0.5f)
        rotateAnimation.duration = 600
        rotateAnimation.repeatCount = Animation.INFINITE
        rotateAnimation.interpolator = LinearInterpolator()
        view1.animation = rotateAnimation
        view1.animate()
    }
}