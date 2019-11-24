package com.ace.homework2.base

import android.view.View
import android.view.animation.AlphaAnimation
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.include_progress_overlay.*

abstract class BaseFragment : DaggerFragment() {

    private lateinit var inAnimation: AlphaAnimation
    private lateinit var outAnimation: AlphaAnimation

    fun loading() {
        inAnimation = AlphaAnimation(0f, 1f)
        inAnimation.duration = 200
        progress_overlay.animation = inAnimation
        progress_overlay.visibility = View.VISIBLE
    }

    fun stopLoading() {
        outAnimation = AlphaAnimation(1f, 0f)
        outAnimation.duration = 200
        progress_overlay.animation = outAnimation
        progress_overlay.visibility = View.GONE
    }
}