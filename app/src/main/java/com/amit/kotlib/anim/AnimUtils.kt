package com.amit.kotlib.anim

import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.os.Build
import android.support.annotation.AnimRes
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils

import com.amit.kotlib.R

/**
 * 2018 May 14 - Monday - 03:05 PM
 * This AnimUtil class will help with animation
 */
object AnimUtils {
    private val TAG = AnimUtils::class.java.simpleName

    /**
     * slide activity from right to left
     * this method will make the activity to slide in from right and slide out from left.
     *
     * @param context - context of the activity
     */
    fun slideActivityFromRightToLeft(context: Context) {
        try {
            (context as Activity).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        } catch (e: Exception) {
            Log.e(TAG, "slideActivityFromRightToLeft: exception while animating activity:\n")
            e.printStackTrace()
        }

    }

    /**
     * slide Activity From Left To Right
     * this activity will make the activity to slide in from left and slide out from right
     *
     * @param context - context of the activity
     */
    fun slideActivityFromLeftToRight(context: Context) {
        try {
            (context as Activity).overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        } catch (e: Exception) {
            Log.e(TAG, "slideActivityFromLeftToRight: exception while animating activity:\n")
            e.printStackTrace()
        }

    }

    /**
     * fade in face out activity
     * this method will make activity fade in or out.
     *
     * @param context - context of the activity
     */
    fun activityFadeInFadeOut(context: Context) {
        try {
            (context as Activity).overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        } catch (e: Exception) {
            Log.e(TAG, "activityFadeInFadeOut: exception while animating acivity.")
            e.printStackTrace()
        }

    }

    /**
     * slide from left with stay anim
     * this method will make activity slide in from left with stay
     *
     * @param context - context of the activity
     */
    fun slideActivityFromLeftWithStay(context: Context) {
        try {
            (context as Activity).overridePendingTransition(R.anim.slide_in_left, R.anim.anim_stay)
        } catch (e: Exception) {
            Log.e(TAG, "slideActivityFromLeftWithStay: exception while animating activity.")
            e.printStackTrace()
        }

    }

    /**
     * slide from right with stay anim
     * this method will make activity slide in from right with stay
     *
     * @param context - context of the activity
     */
    fun slideActivityFromRightWithStay(context: Context) {
        try {
            (context as Activity).overridePendingTransition(R.anim.slide_in_right, R.anim.anim_stay)
        } catch (e: Exception) {
            Log.e(TAG, "slideActivityFromRightWithStay: exception while animating activity.")
            e.printStackTrace()
        }

    }

    /**
     * slide from bottom to up
     * this method will make the activity to slide from bottom to up with stay.
     *
     * @param context - context of the activity
     */
    fun slideActivityFromBottomWithStay(context: Context) {
        try {
            (context as Activity).overridePendingTransition(R.anim.bottom_to_up, R.anim.anim_stay)
        } catch (e: Exception) {
            Log.e(TAG, "slideActivityFromBottomWithStay: exception while animating.")
            e.printStackTrace()
        }

    }

    /**
     * slide from up to bottom
     * this method will make the activity to slide from up to bottom with stay.
     *
     * @param context - context of the activity
     */
    fun slideActivityFromUpWithStay(context: Context) {
        try {
            (context as Activity).overridePendingTransition(R.anim.up_to_bottom, R.anim.anim_stay)
        } catch (e: Exception) {
            Log.e(TAG, "slideFromUpWithStayAnim: exception while animating.")
            e.printStackTrace()
        }

    }

    /**
     * slide from bottom to up
     * this method will make the activity to slide from bottom to up.
     *
     * @param context - context of the activity
     */
    fun slideActivityFromBottomToUp(context: Context) {
        try {
            (context as Activity).overridePendingTransition(R.anim.bottom_to_up, R.anim.up_to_bottom1)
        } catch (e: Exception) {
            Log.e(TAG, "slideActivityFromBottomToUp: exception while animating.")
            e.printStackTrace()
        }

    }

    /**
     * slide from up to bottom
     * this method will make the activity to slide from up to bottom.
     *
     * @param context - context of the activity
     */
    fun slideActivityFromUpToBottom(context: Context) {
        try {
            (context as Activity).overridePendingTransition(R.anim.up_to_bottom, R.anim.bottom_to_up1)
        } catch (e: Exception) {
            Log.e(TAG, "slideActivityFromUpToBottom: exception while animating.")
            e.printStackTrace()
        }

    }

    /**
     * explode transition
     * this method will make any view to explode or popup
     * this method will working with api with or greater then Lollipop
     *
     * @param context - context of the application
     *
     * @param viewGroup - viewGroup on which transition is to be done
     * example: relative layout, linear layout or any view
     *
     * @param duration - duration of the transition
     */
    @TargetApi(21)
    fun explodeTransition(context: Context,
                          viewGroup: ViewGroup,
                          duration: Int) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                viewGroup.visibility = View.VISIBLE
                val animation = AnimationUtils.loadAnimation(context, R.anim.explode)
                animation.duration = duration.toLong()
                viewGroup.animation = animation
                viewGroup.animate()
                animation.start()
            }
        } catch (e: Exception) {
            Log.e(TAG, "explodeTranistion: exception while making transition:\n")
            e.printStackTrace()
        }

    }

    /**
     * slide anim from right
     * this method will make any view slide from right
     *
     * @param context - context of the application
     * @param view - view to animate
     * @param duration - duration of animation
     */
    fun slideAnimFromRight(context: Context,
                           view: View,
                           duration: Int) {
        try {
            view.visibility = View.VISIBLE
            val animation = AnimationUtils.loadAnimation(context, R.anim.slide_in_right)
            animation.duration = duration.toLong()
            view.animate()
            animation.start()
        } catch (e: Exception) {
            Log.e(TAG, "slideAnimFrom: exception while making animation.")
            e.printStackTrace()
        }

    }

    /**
     * slide anim from left
     * this method will make any view slide from right
     *
     * @param context - context of the application
     * @param view - view to animate
     * @param duration - duration of animation
     */
    fun slideAnimFromLeft(context: Context,
                          view: View,
                          duration: Int) {
        try {
            view.visibility = View.VISIBLE
            val animation = AnimationUtils.loadAnimation(context, R.anim.slide_in_left)
            animation.duration = duration.toLong()
            view.animate()
            animation.start()
        } catch (e: Exception) {
            Log.e(TAG, "slideAnimFrom: exception while making animation.")
            e.printStackTrace()
        }

    }

    /**
     * slide anim
     * this method can be used for setting your own slide animation
     *
     * @param context - context of the application
     * @param view - view on which animation is to be performed
     * @param duration - duration of the animation
     * @param animResId - anim resouce for animation
     */
    fun slideAnim(context: Context,
                  view: View,
                  duration: Int,
                  @AnimRes animResId: Int) {
        try {
            view.visibility = View.VISIBLE
            val animation = AnimationUtils.loadAnimation(context, animResId)
            animation.duration = duration.toLong()
            view.animate()
            animation.start()
        } catch (e: Exception) {
            Log.e(TAG, "slideAnim: exception while making animation.")
            e.printStackTrace()
        }

    }

    /**
     * Bounce anim method
     * this method will make a view bounce
     *
     * @param context - context of the application
     * @param view - view to animate
     */
    fun bounceAnim(context: Context, view: View) {
        try {
            val animation = AnimationUtils.loadAnimation(context, R.anim.bounce)
            val interpolator = MyBounceInterpolator(0.2, 20.0)
            animation.interpolator = interpolator
            view.startAnimation(animation)
        } catch (e: Exception) {
            Log.e(TAG, "bounceAnim: exception while making bounce animation.")
            e.printStackTrace()
        }

    }
}
