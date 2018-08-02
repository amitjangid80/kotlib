package com.amit.kotlib.utilities

import android.content.Context
import android.graphics.Typeface
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

object FontHelper {
    private val TAG = FontHelper::class.java.simpleName

    /**
     * apply font method
     *
     * Apply specified font for all views (including nested ones) in the specified root view.
     */
    fun applyFont(context: Context, root: View, fontPath: String) {
        try {
            if (root is ViewGroup) {
                val childCount = root.childCount

                for (i in 0 until childCount) {
                    applyFont(context, root.getChildAt(i), fontPath)
                }
            } else if (root is TextView) {
                root.typeface = Typeface.createFromAsset(context.assets, fontPath)
            }
        } catch (ex: Exception) {
            Log.e(TAG, "Exception in FontHelper class.\n")
            ex.printStackTrace()
        }

    }
}
