package com.amit.kotlib.socialbtn

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.GradientDrawable
import android.support.v7.widget.AppCompatButton
import android.util.AttributeSet

import com.amit.kotlib.R
import com.amit.kotlib.utilities.Utils

open class BaseButton : AppCompatButton {
    private var mIcon: Bitmap? = null
    private var mPaint: Paint? = null
    private var mSrcRect: Rect? = null

    private var txtColor = Color.WHITE
    private var mIconPadding: Int = 0
    private var mIconSize: Int = 0
    private var mRoundedCornerRadius: Int = 0

    private var mIconCenterAligned: Boolean = false
    private var mRoundedCorner: Boolean = false
    private var mTransparentBackground: Boolean = false

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, color: Int, logo: Int) : super(context, attrs, defStyleAttr) {
        init(context, attrs, logo)
        setStyle(color, context)
    }

    constructor(context: Context, attrs: AttributeSet, color: Int, logo: Int) : super(context, attrs) {
        init(context, attrs, logo)
        setStyle(color, context)
    }

    private fun setStyle(color: Int, context: Context) {
        setTextColor(this.txtColor)
        setBackgroundResource(R.drawable.round_corner)

        val drawable = background.mutate() as GradientDrawable
        drawable.setColor(resources.getColor(color))
        drawable.cornerRadius = 0f

        if (mRoundedCorner) {
            drawable.cornerRadius = mRoundedCornerRadius.toFloat()
        }

        if (mTransparentBackground) {
            drawable.setColor(Color.TRANSPARENT)
            drawable.setStroke(4, resources.getColor(color))
        }

        drawable.invalidateSelf()
        setPadding(Utils.convertDpToPixel(30f, context).toInt(),
                0, Utils.convertDpToPixel(30f, context).toInt(),
                0)
    }

    override fun onDraw(canvas: Canvas) {
        // Recalculate width and amount to shift by, taking into account icon size
        val shift = (mIconSize + mIconPadding) / 2
        canvas.save()
        canvas.translate(shift.toFloat(), 0f)
        super.onDraw(canvas)

        val textWidth = paint.measureText(text.toString())
        var left = (width / 2f - textWidth / 2f - mIconSize.toFloat() - mIconPadding.toFloat()).toInt()
        val top = height / 2 - mIconSize / 2

        if (!mIconCenterAligned) {
            left = 0
        }

        val destRect = Rect(left, top, left + mIconSize, top + mIconSize)
        canvas.drawBitmap(mIcon!!, mSrcRect, destRect, mPaint)
        canvas.restore()
    }

    private fun init(context: Context, attrs: AttributeSet, logo: Int) {
        val array = context.obtainStyledAttributes(attrs, R.styleable.BaseButton)

        // Initialize variables to default values
        setDefaultValues(context, logo)

        // Don't add padding when text isn't present
        if (attrs.getAttributeValue("http://schemas.android.com/apk/res/android", "text") != null) {
            mIconPadding = Utils.convertDpToPixel(20f, context).toInt()
        }

        txtColor = array.getColor(R.styleable.BaseButton_android_textColor, Color.WHITE)

        // Load the custom properties and assign values
        for (i in 0 until array.indexCount) {
            val attr = array.getIndex(i)

            if (attr == R.styleable.BaseButton_iconPadding) {
                mIconPadding = array.getDimensionPixelSize(attr, Utils.convertDpToPixel(20f, context).toInt())
            }

            if (attr == R.styleable.BaseButton_iconCenterAligned) {
                mIconCenterAligned = array.getBoolean(attr, true)
            }

            if (attr == R.styleable.BaseButton_iconSize) {
                mIconSize = array.getDimensionPixelSize(attr, Utils.convertDpToPixel(20f, context).toInt())
            }

            if (attr == R.styleable.BaseButton_roundedCorner) {
                mRoundedCorner = array.getBoolean(attr, false)
            }

            if (attr == R.styleable.BaseButton_roundedCornerRadius) {
                mRoundedCornerRadius = array.getDimensionPixelSize(attr, Utils.convertDpToPixel(40f, context).toInt())
            }

            if (attr == R.styleable.BaseButton_transparentBackground) {
                mTransparentBackground = array.getBoolean(attr, false)
            }
        }

        array.recycle()

        if (mIcon != null) {
            mPaint = Paint()
            mSrcRect = Rect(0, 0, mIcon!!.width, mIcon!!.height)
        }
    }

    private fun setDefaultValues(context: Context, logo: Int) {
        mIcon = Utils.drawableToBitmap(resources.getDrawable(logo))
        mIconSize = Utils.convertDpToPixel(20f, context).toInt()
        mIconCenterAligned = true
        mRoundedCorner = false
        mTransparentBackground = false
        mRoundedCornerRadius = Utils.convertDpToPixel(40f, context).toInt()
    }
}
