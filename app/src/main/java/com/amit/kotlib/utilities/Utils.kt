package com.amit.kotlib.utilities

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.support.annotation.AttrRes
import android.support.annotation.CheckResult
import android.support.annotation.ColorRes
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.TextUtils
import android.text.style.StyleSpan
import android.util.Base64
import android.util.DisplayMetrics
import android.util.Log
import android.view.inputmethod.InputMethodManager
import java.net.MalformedURLException
import java.net.URISyntaxException
import java.net.URL
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

/**
 * https://github.com/jaydeepw/android-utils/tree/master/Utils
**/
@Suppress("unused", "MemberVisibilityCanBePrivate")
object Utils
{
    private val TAG = Utils::class.java.simpleName
    private var xdpi = java.lang.Float.MIN_VALUE

    /**
     * is url valid
     * this method will check if the url provided is valid or not
     *
     * @param url - url to check
     * @return - will return true if the url is valid
     * else will return false
    **/
    @CheckResult
    fun isUrlValid(url: String): Boolean
    {
        val urlObj: URL

        try
        {
            urlObj = URL(url)
        }
        catch (e: MalformedURLException)
        {
            Log.e(TAG, "isUrlValid: excepion while checking valid url.")
            e.printStackTrace()
            return false
        }

        try
        {
            urlObj.toURI()
        }
        catch (e: URISyntaxException)
        {
            Log.e(TAG, "isUrlValid: uri syntax exception.")
            e.printStackTrace()
            return false
        }

        return true
    }

    /**
     * to Bold
     * this method will convert the normal text to bold
     *
     * @param sourceText - text to convert to bold
     *
     * @return - [android.text.SpannableString] in BOLD TypeFace
    **/
    fun toBold(sourceText: String?): SpannableStringBuilder?
    {
        try
        {
            if (sourceText != null)
            {
                val sb = SpannableStringBuilder(sourceText)

                // span to set text color to some RGB value
                val bss = StyleSpan(Typeface.BOLD)

                // set text bold
                sb.setSpan(bss, 0, sb.length, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
                return sb
            }
            else
            {
                throw NullPointerException("String to convert cannot be null.")
            }
        }
        catch (e: Exception)
        {
            Log.e(TAG, "toBold: exception while making the text bold.")
            e.printStackTrace()
            return null
        }
    }

    /**
     * to Bold
     * this method will convert a string or a sub string to bold
     *
     * @param string - string in which the sub string has to be converted to bold
     * or string to be converted to bold
     *
     * @param subString - The subString within the string to bold.
     * Pass null to bold entire string.
     *
     * @return - [android.text.SpannableString] in Bold TypeFace
    **/
    fun toBold(string: String, subString: String?): SpannableStringBuilder?
    {
        try
        {
            if (TextUtils.isEmpty(string))
            {
                return SpannableStringBuilder("")
            }

            val spannableBuilder = SpannableStringBuilder(string)
            val bss = StyleSpan(Typeface.BOLD)

            if (subString != null)
            {
                val subStringNameStart = string.toLowerCase().indexOf(subString)

                if (subStringNameStart > -1)
                {
                    spannableBuilder.setSpan(
                            bss, subStringNameStart,
                            subStringNameStart + subString.length,
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                }
            }
            else
            {
                // set the entire text to bold
                spannableBuilder.setSpan(
                        bss, 0,
                        spannableBuilder.length,
                        Spannable.SPAN_INCLUSIVE_INCLUSIVE)
            }

            return spannableBuilder
        }
        catch (e: Exception)
        {
            Log.e(TAG, "toBold: exception while converting string or sub string to bold.")
            e.printStackTrace()
            return null
        }
    }

    /**
     * hide keyboard
     * this method will hide the keyboard
     *
     * @param context - context of the application
    **/
    fun hideKeyboard(context: Context)
    {
        try
        {
            val inputManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

            inputManager.hideSoftInputFromWindow(
                    (context as Activity).currentFocus!!.windowToken,
                    InputMethodManager.HIDE_NOT_ALWAYS)
        }
        catch (e: Exception)
        {
            Log.e(TAG, "hideKeyboard: exception while hiding keyboard.")
            e.printStackTrace()
        }
    }

    /**
     * get sha 512 hash
     * this method will convert a string to byte array.
     *
     * @param stringToHash - string to convert to hash.
     *
     * @return string converted to hash value.
    **/
    fun getSha512Hash(stringToHash: String?): String
    {
        return try
        {
            if (stringToHash == null)
            {
                ""
            }
            else getSha512Hash(stringToHash.toByteArray())
        }
        catch (e: Exception)
        {
            Log.e(TAG, "getSha512Hash: exception while getting sha 512 hash.")
            e.printStackTrace()
            ""
        }
    }

    /**
     * get sha 512 hash
     * this method will convert the byte array to string
     * which is converted to hash
     *
     * @param dataToHash - byte array to convert to hash value
     *
     * @return string converted into hash value.
    **/
    fun getSha512Hash(dataToHash: ByteArray): String
    {
        var md: MessageDigest? = null

        try
        {
            md = MessageDigest.getInstance("SHA-512")
        }
        catch (e: NoSuchAlgorithmException)
        {
            Log.e(TAG, "getSha512Hash: exception while getting sha 512 hash.")
            e.printStackTrace()
        }

        if (md != null)
        {
            md.update(dataToHash)
            val byteData = md.digest()
            return Base64.encodeToString(byteData, Base64.DEFAULT)
        }

        return ""
    }

    /**
     * 2018 May 14 - Monday - 05:50 PM
     * get drawable
     * this method will get you the drawables
     *
     * @param context - context of the application
     * @param id - drawable id
     *
     * @return returns drawable
    **/
    fun getDrawable(context: Context, id: Int): Drawable?
    {
        return when
        {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> context.getDrawable(id)
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP -> context.resources.getDrawable(id, null)
            else -> null
        }
    }

    /**
     * get Color wrapper
     * this method will get the color resource based on android version
     *
     * @param context - context of the application
     * @param id - id of the color resource
     *
     * @return int - color in integer.
    **/
    fun getColorWrapper(context: Context, @ColorRes id: Int): Int
    {
        return when
        {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> context.getColor(id)
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> context.resources.getColor(id, null)
            else -> 0
        }
    }

    /**
     * dp to px
     * this method will convert dp to pixles
     *
     * @param context - context of the application
     * @param dp - dp to convert into pixels
     *
     * @return - pixels in integer form
    **/
    fun dpToPx(context: Context, dp: Int): Int
    {
        if (xdpi == java.lang.Float.MIN_VALUE)
        {
            xdpi = context.resources.displayMetrics.xdpi
        }

        return Math.round(dp * (xdpi / DisplayMetrics.DENSITY_DEFAULT))
    }

    fun getThemeAttrDrawable(context: Context, @AttrRes attributeDrawable: Int): Drawable?
    {
        val attrs = intArrayOf(attributeDrawable)
        val ta = context.obtainStyledAttributes(attrs)
        val drawableFromTheme = ta.getDrawable(0)
        ta.recycle()
        return drawableFromTheme
    }

    fun getThemeAttrColor(context: Context, @AttrRes attributeColor: Int): Int
    {
        val attrs = intArrayOf(attributeColor)
        val ta = context.obtainStyledAttributes(attrs)
        val color = ta.getColor(0, Color.TRANSPARENT)
        ta.recycle()
        return color
    }

    /**
     * convert dp to pixel
     * this method will convert dp to pixels
     *
     * @param context - context of the application
     * @param dp - dp to convert into pixels
     *
     * @return - pixels in integer form
    **/
    fun convertDpToPixel(dp: Float, context: Context): Float
    {
        val resources = context.resources
        val metrics = resources.displayMetrics
        return dp * (metrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
    }

    /**
     * convert pixels to dp
     * this method will converts pixels to dp
     *
     * @param context - context of the application
     * @param px - pixels to be convert into dp
     *
     * @return - dp in float form
    **/
    fun convertPixelsToDp(px: Float, context: Context): Float
    {
        val resources = context.resources
        val metrics = resources.displayMetrics
        return px / (metrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
    }

    /**
     * drawable to bitmap
     * this method will convert a drawable to bitmap
     *
     * @param drawable - drawable to be converted into bitmap
     * @return bitmap
    **/
    fun drawableToBitmap(drawable: Drawable): Bitmap
    {
        if (drawable is BitmapDrawable)
        {
            return drawable.bitmap
        }

        val bitmap = Bitmap.createBitmap(
                drawable.intrinsicWidth,
                drawable.intrinsicHeight,
                Bitmap.Config.ARGB_8888)

        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return bitmap
    }

    /**
     * dp 2 px
     * this method will convert dp to pixels
     *
     * @param context - context of the application
     * @param dpValue - dpValue to be convert into pixels
     *
     * @return - pixels in integer form
    **/
    fun dp2px(context: Context, dpValue: Float): Int
    {
        val scale = context.resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }

    /**
     * convert pixels to dp
     * this method will converts pixels to dp
     *
     * @param context - context of the application
     * @param pxValue - pxValue to be convert into dp
     *
     * @return - dp in float form
    **/
    fun px2dp(context: Context, pxValue: Float): Int
    {
        val scale = context.resources.displayMetrics.density
        return (pxValue / scale + 0.5f).toInt()
    }

    /**
     * 2018 June 23 - Saturday - 10:30 AM
     * right padding method
     *
     * this method will append empty or blank or spaces
     * after the string for specified length.
     *
     * @param strText - String text to append spaces to the right
     * @param length - length of the string text including spaces and text.
     *
     * @return - returns the string with spaces appended to the right of the string
    **/
    fun rightPadding(strText: String, length: Int): String
    {
        return String.format("%-" + length + "." + length + "s", strText)
    }

    /**
     * 2018 June 23 - Saturday - 10:30 AM
     * left padding method
     *
     * this method will append empty or blank or spaces
     * after the string for specified length.
     *
     * @param strText - String text to append spaces to the left
     * @param length - length of the string text including spaces and text.
     *
     * @return - returns the string with spaces appended to the left of the string.
    **/
    fun leftPadding(strText: String, length: Int): String
    {
        return String.format("%" + length + "." + length + "s", strText)
    }
}
