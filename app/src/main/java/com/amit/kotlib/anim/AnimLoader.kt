package com.amit.kotlib.anim

import android.content.Context
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.AnimationSet
import android.view.animation.ScaleAnimation

/**
 * Created by Amit Jangid on 22,May,2018
**/
object AnimLoader
{
    fun getInAnimation(context: Context): AnimationSet
    {
        val `in` = AnimationSet(context, null)
        val alpha = AlphaAnimation(0.0f, 1.0f)
        alpha.duration = 90

        val scale1 = ScaleAnimation(
                0.8f, 1.05f, 0.8f, 1.05f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f)

        scale1.duration = 135

        val scale2 = ScaleAnimation(
                1.05f, 0.95f, 1.05f, 0.95f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f)

        scale2.duration = 105
        scale2.startOffset = 135

        val scale3 = ScaleAnimation(
                0.95f, 1f, 0.95f, 1.0f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f)

        scale3.duration = 60
        scale3.startOffset = 240

        `in`.addAnimation(alpha)
        `in`.addAnimation(scale1)
        `in`.addAnimation(scale2)
        `in`.addAnimation(scale3)

        return `in`
    }

    fun getOutAnimation(context: Context): AnimationSet
    {
        val out = AnimationSet(context, null)
        val alpha = AlphaAnimation(1.0f, 0.0f)
        alpha.duration = 150

        val scale = ScaleAnimation(
                1.0f, 0.6f, 1.0f, 0.6f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f)

        scale.duration = 150
        out.addAnimation(alpha)
        out.addAnimation(scale)
        return out
    }
}
