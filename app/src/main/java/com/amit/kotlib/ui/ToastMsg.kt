package com.amit.kotlib.ui

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.support.annotation.CheckResult
import android.support.annotation.ColorInt
import android.support.annotation.DrawableRes
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast

import com.amit.kotlib.R

@SuppressLint("InflateParams")
class ToastMsg {

    enum class ToastPosition {
        TOP, BOTTOM, CENTER
    }

    class Config private constructor()// avoiding instantiation
    {
        @ColorInt
        private var DEFAULT_TEXT_COLOR = ToastMsg.DEFAULT_TEXT_COLOR

        @ColorInt
        private var ERROR_COLOR = ToastMsg.ERROR_COLOR

        @ColorInt
        private var INFO_COLOR = ToastMsg.INFO_COLOR

        @ColorInt
        private var SUCCESS_COLOR = ToastMsg.SUCCESS_COLOR

        @ColorInt
        private var WARNING_COLOR = ToastMsg.WARNING_COLOR

        private var typeface = ToastMsg.currentTypeFace
        private var tintIcon = ToastMsg.tintIcon
        private var textSize = ToastMsg.textSize

        @CheckResult
        fun setTextColor(@ColorInt textColor: Int): Config {
            DEFAULT_TEXT_COLOR = textColor
            return this
        }

        @CheckResult
        fun setErrorColor(@ColorInt errorColor: Int): Config {
            ERROR_COLOR = errorColor
            return this
        }

        @CheckResult
        fun setInfoColor(@ColorInt infoColor: Int): Config {
            INFO_COLOR = infoColor
            return this
        }

        @CheckResult
        fun setSuccessColor(@ColorInt successColor: Int): Config {
            SUCCESS_COLOR = successColor
            return this
        }

        @CheckResult
        fun setWarningColor(@ColorInt warningColor: Int): Config {
            WARNING_COLOR = warningColor
            return this
        }

        @CheckResult
        fun setToastTypeface(typeface: Typeface): Config {
            this.typeface = typeface
            return this
        }

        @CheckResult
        fun setTextSize(sizeInSp: Int): Config {
            this.textSize = sizeInSp
            return this
        }

        @CheckResult
        fun tintIcon(tintIcon: Boolean): Config {
            this.tintIcon = tintIcon
            return this
        }

        fun apply() {
            ToastMsg.DEFAULT_TEXT_COLOR = DEFAULT_TEXT_COLOR
            ToastMsg.ERROR_COLOR = ERROR_COLOR
            ToastMsg.INFO_COLOR = INFO_COLOR
            ToastMsg.SUCCESS_COLOR = SUCCESS_COLOR
            ToastMsg.WARNING_COLOR = WARNING_COLOR
            ToastMsg.currentTypeFace = typeface
            ToastMsg.textSize = textSize
            ToastMsg.tintIcon = tintIcon
        }

        companion object {

            val instance: Config
                @CheckResult
                get() = Config()

            fun reset() {
                ToastMsg.DEFAULT_TEXT_COLOR = Color.parseColor("#FFFFFF")
                ToastMsg.ERROR_COLOR = Color.parseColor("#D50000")
                ToastMsg.INFO_COLOR = Color.parseColor("#3F51B5")
                ToastMsg.SUCCESS_COLOR = Color.parseColor("#388E3C")
                ToastMsg.WARNING_COLOR = Color.parseColor("#FFA900")
                ToastMsg.currentTypeFace = LOADED_TOAST_TYPEFACE
                ToastMsg.textSize = 16
                ToastMsg.tintIcon = true
            }
        }
    }

    companion object {
        @ColorInt
        private var DEFAULT_TEXT_COLOR = Color.parseColor("#FFFFFF")

        @ColorInt
        private var ERROR_COLOR = Color.parseColor("#D50000")

        @ColorInt
        private var INFO_COLOR = Color.parseColor("#3F51B5")

        @ColorInt
        private var SUCCESS_COLOR = Color.parseColor("#388E3C")

        @ColorInt
        private var WARNING_COLOR = Color.parseColor("#FFA900")

        @ColorInt
        private val NORMAL_COLOR = Color.parseColor("#353A3E")

        private val LOADED_TOAST_TYPEFACE = Typeface.create("sans-serif-condensed", Typeface.NORMAL)

        private var currentTypeFace = LOADED_TOAST_TYPEFACE
        private var textSize = 16 // in SP
        private var tintIcon = true

        @CheckResult
        fun normal(context: Context,
                   message: CharSequence,
                   toastPosition: ToastPosition): Toast {
            return normal(context, message, Toast.LENGTH_SHORT, null, false, toastPosition)
        }

        @CheckResult
        fun normal(context: Context,
                   message: CharSequence,
                   icon: Drawable,
                   toastPosition: ToastPosition): Toast {
            return normal(context, message, Toast.LENGTH_SHORT, icon, true, toastPosition)
        }

        @CheckResult
        fun normal(context: Context,
                   message: CharSequence,
                   duration: Int,
                   toastPosition: ToastPosition): Toast {
            return normal(context, message, duration, null, false, toastPosition)
        }

        @CheckResult
        fun normal(context: Context,
                   message: CharSequence,
                   duration: Int,
                   icon: Drawable,
                   toastPosition: ToastPosition): Toast {
            return normal(context, message, duration, icon, true, toastPosition)
        }

        @CheckResult
        fun normal(context: Context,
                   message: CharSequence,
                   duration: Int, icon: Drawable?,
                   withIcon: Boolean,
                   toastPosition: ToastPosition): Toast {
            return custom(context, message, icon, NORMAL_COLOR,
                    duration, withIcon, true, toastPosition)
        }

        @CheckResult
        fun warning(context: Context,
                    message: CharSequence,
                    toastPosition: ToastPosition): Toast {
            return warning(context, message, Toast.LENGTH_SHORT, true, toastPosition)
        }

        @CheckResult
        fun warning(context: Context,
                    message: CharSequence,
                    duration: Int,
                    toastPosition: ToastPosition): Toast {
            return warning(context, message, duration, true, toastPosition)
        }

        @CheckResult
        fun warning(context: Context,
                    message: CharSequence,
                    duration: Int,
                    withIcon: Boolean,
                    toastPosition: ToastPosition): Toast {
            return custom(context, message,
                    ToastMsgUtils.getDrawable(context, R.drawable.ic_error_outline_white_48dp),
                    WARNING_COLOR,
                    duration,
                    withIcon,
                    true,
                    toastPosition)
        }

        @CheckResult
        fun info(context: Context,
                 message: CharSequence,
                 toastPosition: ToastPosition): Toast {
            return info(context, message, Toast.LENGTH_SHORT, true, toastPosition)
        }

        @CheckResult
        fun info(context: Context,
                 message: CharSequence,
                 duration: Int,
                 toastPosition: ToastPosition): Toast {
            return info(context, message, duration, true, toastPosition)
        }

        @CheckResult
        fun info(context: Context,
                 message: CharSequence,
                 duration: Int,
                 withIcon: Boolean,
                 toastPosition: ToastPosition): Toast {
            return custom(context, message,
                    ToastMsgUtils.getDrawable(context, R.drawable.ic_info_outline_white_48dp),
                    INFO_COLOR,
                    duration,
                    withIcon,
                    true,
                    toastPosition)
        }

        @CheckResult
        fun success(context: Context,
                    message: CharSequence,
                    toastPosition: ToastPosition): Toast {
            return success(context, message, Toast.LENGTH_SHORT, true, toastPosition)
        }

        @CheckResult
        fun success(context: Context,
                    message: CharSequence,
                    duration: Int,
                    toastPosition: ToastPosition): Toast {
            return success(context, message, duration, true, toastPosition)
        }

        @CheckResult
        fun success(context: Context,
                    message: CharSequence,
                    duration: Int,
                    withIcon: Boolean,
                    toastPosition: ToastPosition): Toast {
            return custom(context, message,
                    ToastMsgUtils.getDrawable(context, R.drawable.ic_check_white_48dp),
                    SUCCESS_COLOR,
                    duration,
                    withIcon,
                    true,
                    toastPosition)
        }

        @CheckResult
        fun error(context: Context,
                  message: CharSequence,
                  toastPosition: ToastPosition): Toast {
            return error(context, message, Toast.LENGTH_SHORT, true, toastPosition)
        }

        @CheckResult
        fun error(context: Context,
                  message: CharSequence,
                  duration: Int,
                  toastPosition: ToastPosition): Toast {
            return error(context, message, duration, true, toastPosition)
        }

        @CheckResult
        fun error(context: Context,
                  message: CharSequence,
                  duration: Int,
                  withIcon: Boolean,
                  toastPosition: ToastPosition): Toast {
            return custom(context, message,
                    ToastMsgUtils.getDrawable(context, R.drawable.ic_clear_white_48dp),
                    ERROR_COLOR,
                    duration,
                    withIcon,
                    true,
                    toastPosition)
        }

        @CheckResult
        fun custom(context: Context,
                   message: CharSequence,
                   icon: Drawable,
                   duration: Int,
                   withIcon: Boolean,
                   toastPosition: ToastPosition): Toast {
            return custom(context, message, icon,
                    -1,
                    duration,
                    withIcon,
                    false,
                    toastPosition)
        }

        @CheckResult
        fun custom(context: Context,
                   message: CharSequence,
                   @DrawableRes iconRes: Int,
                   @ColorInt tintColor: Int,
                   duration: Int, withIcon: Boolean,
                   shouldTint: Boolean,
                   toastPosition: ToastPosition): Toast {
            return custom(context, message,
                    ToastMsgUtils.getDrawable(context, iconRes),
                    tintColor,
                    duration,
                    withIcon,
                    shouldTint,
                    toastPosition)
        }

        @SuppressLint("ShowToast")
        @CheckResult
        fun custom(context: Context,
                   message: CharSequence,
                   icon: Drawable?,
                   @ColorInt tintColor: Int,
                   duration: Int,
                   withIcon: Boolean,
                   shouldTint: Boolean,
                   toastPosition: ToastPosition): Toast {
            var icon = icon
            val currentToast = Toast(context)

            val toastLayout = (context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater)
                    .inflate(R.layout.toast_message_layout, null)

            val toastIcon = toastLayout.findViewById(R.id.toastIcon) as ImageView
            val tvToastMessage = toastLayout.findViewById(R.id.tvToastMessage) as TextView

            val drawableFrame: Drawable

            if (shouldTint) {
                // drawableFrame = ToastMsgUtils.tint9PatchDrawableFrame(context, tintColor);
                drawableFrame = ToastMsgUtils.getDrawable(context, R.drawable.toast_background)
                drawableFrame.setColorFilter(tintColor, PorterDuff.Mode.ADD)
            } else {
                // drawableFrame = ToastMsgUtils.getDrawable(context, R.drawable.toast_frame);
                drawableFrame = ToastMsgUtils.getDrawable(context, R.drawable.toast_background)
            }

            ToastMsgUtils.setBackground(toastLayout, drawableFrame)

            if (withIcon) {
                if (icon == null) {
                    throw IllegalArgumentException("Avoid passing 'icon' as null if 'withIcon' is set to true.")
                }

                if (tintIcon) {
                    icon = ToastMsgUtils.tintIcon(icon, DEFAULT_TEXT_COLOR)
                }

                ToastMsgUtils.setBackground(toastIcon, icon)
            } else {
                toastIcon.setVisibility(View.GONE)
            }

            when (toastPosition) {
                ToastMsg.ToastPosition.TOP -> currentToast.setGravity(Gravity.TOP, 0, 0)

                ToastMsg.ToastPosition.BOTTOM -> currentToast.setGravity(Gravity.BOTTOM, 0, 0)

                ToastMsg.ToastPosition.CENTER -> currentToast.setGravity(Gravity.CENTER, 0, 0)

                else -> {
                }
            }

            tvToastMessage.setText(message)
            tvToastMessage.setTextColor(DEFAULT_TEXT_COLOR)
            tvToastMessage.setTypeface(currentTypeFace)
            tvToastMessage.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize.toFloat())

            currentToast.duration = duration
            currentToast.view = toastLayout
            return currentToast
        }
    }
}
// avoiding instantiation
