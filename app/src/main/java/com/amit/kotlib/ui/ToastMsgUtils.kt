package com.amit.kotlib.ui

import android.content.Context
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.graphics.drawable.NinePatchDrawable
import android.os.Build
import android.support.annotation.ColorInt
import android.support.annotation.DrawableRes
import android.view.View
import com.amit.kotlib.R

/**
 * Created by AMIT JANGID
 * 2018 April 21 - Saturday - 05:00 PM
 *
 * Toast message utils class
 */
object ToastMsgUtils {
    internal fun tintIcon(drawable: Drawable, @ColorInt tintColor: Int): Drawable {
        drawable.setColorFilter(tintColor, PorterDuff.Mode.SRC_IN)
        return drawable
    }

    internal fun tint9PatchDrawableFrame(context: Context, @ColorInt tintColor: Int): Drawable {
        val toastDrawable = getDrawable(context, R.drawable.toast_frame) as NinePatchDrawable?
        return tintIcon(toastDrawable!!, tintColor)
    }

    internal fun setBackground(view: View, drawable: Drawable) {
        view.background = drawable
    }

    internal fun getDrawable(context: Context, @DrawableRes id: Int): Drawable {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // context.getDrawable(id)
            context.resources.getDrawable(id, null)
        } else {
            context.resources.getDrawable(id)
        }
    }
}
