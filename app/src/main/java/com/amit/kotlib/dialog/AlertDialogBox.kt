package com.amit.kotlib.dialog

import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.GradientDrawable
import android.support.annotation.ColorInt
import android.support.annotation.DrawableRes
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView

import com.amit.kotlib.R

import com.amit.kotlib.dialog.Anim.POP
import com.amit.kotlib.dialog.Anim.SIDE
import com.amit.kotlib.dialog.Anim.SLIDE

/**
 * Created by Amit Jangid on 21,May,2018
 */
class AlertDialogBox private constructor(builder: Builder) {
    private val Anim: Anim?
    private val cancel: Boolean
    private val visibility: Icon?
    private val activity: Activity

    private val pListener: AlertDialogListener?
    private val nListener: AlertDialogListener?
    private val title: String?
    private val message: String?
    private val positiveBtnText: String?
    private val negativeBtnText: String?

    @DrawableRes
    private val icon: Int

    @ColorInt
    private val pBtnColor: Int
    @ColorInt
    private val nBtnColor: Int
    @ColorInt
    private val bgColor: Int
    @ColorInt
    private val pBtnTextColor: Int
    @ColorInt
    private val nBtnTextColor: Int

    init {
        this.icon = builder.icon
        this.Anim = builder.Anim
        this.title = builder.title
        this.cancel = builder.cancel

        this.message = builder.message
        this.bgColor = builder.bgColor
        this.activity = builder.activity

        this.pListener = builder.pListener
        this.nListener = builder.nListener

        this.pBtnColor = builder.pBtnColor
        this.nBtnColor = builder.nBtnColor
        this.visibility = builder.visibility

        this.pBtnTextColor = builder.pBtnTextColor
        this.nBtnTextColor = builder.nBtnTextColor

        this.positiveBtnText = builder.positiveBtnText
        this.negativeBtnText = builder.negativeBtnText
    }

    class Builder(val activity: Activity) {
        var Anim: Anim? = null
        var visibility: Icon? = null

        var cancel: Boolean = false
        var pListener: AlertDialogListener? = null
        var nListener: AlertDialogListener? = null
        var title: String? = null
        var message: String? = null
        var positiveBtnText: String? = null
        var negativeBtnText: String? = null

        @DrawableRes
        var icon: Int = 0
        @ColorInt
        var pBtnColor: Int = 0
        @ColorInt
        var pBtnTextColor: Int = 0
        @ColorInt
        var nBtnColor: Int = 0
        @ColorInt
        var nBtnTextColor: Int = 0
        @ColorInt
        var bgColor: Int = 0

        fun setTitle(title: String): Builder {
            this.title = title
            return this
        }

        fun setBackgroundColor(bgColor: Int): Builder {
            this.bgColor = bgColor
            return this
        }

        fun setMessage(message: String): Builder {
            this.message = message
            return this
        }

        fun setPositiveBtnText(positiveBtnText: String): Builder {
            this.positiveBtnText = positiveBtnText
            return this
        }

        fun setPositiveBtnTextColor(pBtnTextColor: Int): Builder {
            this.pBtnTextColor = pBtnTextColor
            return this
        }

        fun setPositiveBtnBackground(pBtnColor: Int): Builder {
            this.pBtnColor = pBtnColor
            return this
        }

        fun setNegativeBtnText(negativeBtnText: String): Builder {
            this.negativeBtnText = negativeBtnText
            return this
        }

        fun setNegativeBtnTextColor(nBtnTextColor: Int): Builder {
            this.nBtnTextColor = nBtnTextColor
            return this
        }

        fun setNegativeBtnBackground(nBtnColor: Int): Builder {
            this.nBtnColor = nBtnColor
            return this
        }

        //setIcon
        fun setIcon(icon: Int, visibility: Icon): Builder {
            this.icon = icon
            this.visibility = visibility
            return this
        }

        fun setAnim(Anim: Anim): Builder {
            this.Anim = Anim
            return this
        }

        //set Positive listener
        fun onPositiveClicked(pListener: AlertDialogListener): Builder {
            this.pListener = pListener
            return this
        }

        //set Negative listener
        fun onNegativeClicked(nListener: AlertDialogListener): Builder {
            this.nListener = nListener
            return this
        }

        fun isCancellable(cancel: Boolean): Builder {
            this.cancel = cancel
            return this
        }

        fun build(): AlertDialogBox {
            val message1: TextView
            val title1: TextView
            val iconImg: ImageView
            val nBtn: Button
            val pBtn: Button
            val view: View

            val dialog: Dialog

            if (Anim == POP) {
                dialog = Dialog(activity, R.style.PopTheme)
            } else if (Anim == SIDE) {
                dialog = Dialog(activity, R.style.SideTheme)
            } else if (Anim == SLIDE) {
                dialog = Dialog(activity, R.style.SlideTheme)
            } else {
                dialog = Dialog(activity)
            }

            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.setCancelable(cancel)
            dialog.setContentView(R.layout.custom_alert_dialog)

            //getting resources
            view = dialog.findViewById(R.id.background)
            title1 = dialog.findViewById(R.id.title)
            message1 = dialog.findViewById(R.id.message)
            iconImg = dialog.findViewById(R.id.icon)
            nBtn = dialog.findViewById(R.id.negativeBtn)
            pBtn = dialog.findViewById(R.id.positiveBtn)
            title1.text = title
            message1.text = message

            if (positiveBtnText != null) {
                pBtn.text = positiveBtnText
            }

            if (pBtnColor != 0) {
                val bgShape = pBtn.background as GradientDrawable
                bgShape.setColor(pBtnColor)
            }

            if (pBtnTextColor != 0) {
                pBtn.setTextColor(pBtnTextColor)
            }

            if (nBtnColor != 0) {
                val bgShape = nBtn.background as GradientDrawable
                bgShape.setColor(nBtnColor)
            }

            if (nBtnTextColor != 0) {
                nBtn.setTextColor(nBtnTextColor)
            }

            if (negativeBtnText != null) {
                nBtn.text = negativeBtnText
            }

            iconImg.setImageResource(icon)

            if (visibility === Icon.Visible) {
                iconImg.visibility = View.VISIBLE
            } else {
                iconImg.visibility = View.GONE
            }

            if (bgColor != 0) {
                view.setBackgroundColor(bgColor)
            }

            if (pListener != null) {
                pBtn.setOnClickListener {
                    pListener!!.onClick()
                    dialog.dismiss()
                }
            } else {
                pBtn.setOnClickListener { dialog.dismiss() }
            }

            if (nListener != null) {
                nBtn.visibility = View.VISIBLE

                nBtn.setOnClickListener {
                    nListener!!.onClick()
                    dialog.dismiss()
                }
            }

            dialog.show()
            return AlertDialogBox(this)
        }
    }
}
