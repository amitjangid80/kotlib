package com.amit.kotlib.shinebtn

import android.animation.ValueAnimator
import android.graphics.Canvas

import com.amit.kotlib.anim.Ease
import com.amit.kotlib.anim.EaseInterpolator

@Suppress("unused", "UNUSED_PARAMETER")
class ShineAnimator : ValueAnimator
{
    private val maxValue = 1.5f
    private val animDuration: Long = 1500
    private var canvas: Canvas? = null

    internal constructor()
    {
        setFloatValues(1f, maxValue)
        duration = animDuration
        startDelay = 200
        interpolator = EaseInterpolator(Ease.QUART_OUT)
    }

    internal constructor(duration: Long, max_value: Float, delay: Long)
    {
        setFloatValues(1f, max_value)
        setDuration(duration)
        startDelay = delay
        interpolator = EaseInterpolator(Ease.QUART_OUT)
    }

    fun startAnim(shineView: ShineView, centerAnimX: Int, centerAnimY: Int)
    {
        start()
    }

    fun setCanvas(canvas: Canvas)
    {
        this.canvas = canvas
    }
}
