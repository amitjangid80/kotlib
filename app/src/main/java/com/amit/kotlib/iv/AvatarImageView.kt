package com.amit.kotlib.iv

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.util.AttributeSet

import com.amit.kotlib.R

class AvatarImageView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : CircularImageView(context, attrs) {
    private val mTextPaint: Paint
    private val mTextBounds: Rect

    private val mBackgroundPaint: Paint
    private val mBackgroundBounds: RectF

    private var mShowState: Int = 0
    private var mInitial: String? = null

    var initial: String?
        get() = mInitial
        set(letter) {
            mInitial = extractInitial(letter)
            updateTextBounds()
            invalidate()
        }

    var state: Int
        get() = mShowState
        set(state) {
            if (state != SHOW_INITIAL && state != SHOW_IMAGE) {
                val msg = "Illegal avatar state value: $state, use either SHOW_INITIAL or SHOW_IMAGE constant."
                throw IllegalArgumentException(msg)
            }

            mShowState = state
            invalidate()
        }

    var textSize: Float
        get() = mTextPaint.textSize
        set(size) {
            mTextPaint.textSize = size
            updateTextBounds()
            invalidate()
        }

    var textColor: Int
        get() = mTextPaint.color
        set(color) {
            mTextPaint.color = color
            invalidate()
        }

    var avatarBackgroundColor: Int
        get() = mBackgroundPaint.color
        set(color) {
            mBackgroundPaint.color = color
            invalidate()
        }

    init {

        var showState = DEF_STATE
        var textColor = Color.WHITE
        var textSize = DEF_TEXT_SIZE
        var initial: String? = DEF_INITIAL
        var backgroundColor = DEF_BACKGROUND_COLOR

        if (attrs != null) {
            val a = context.obtainStyledAttributes(attrs, R.styleable.AvatarImageView, 0, 0)
            initial = a.getString(R.styleable.AvatarImageView_avatar_text)
            showState = a.getInt(R.styleable.AvatarImageView_avatar_state, showState)
            textColor = a.getColor(R.styleable.AvatarImageView_avatar_textColor, textColor)
            textSize = a.getDimensionPixelSize(R.styleable.AvatarImageView_avatar_textSize, textSize)
            backgroundColor = a.getColor(R.styleable.AvatarImageView_avatar_backgroundColor, backgroundColor)
            a.recycle()
        }

        mShowState = showState
        mTextPaint = Paint(Paint.ANTI_ALIAS_FLAG)

        mTextPaint.color = textColor
        mTextPaint.textSize = textSize.toFloat()
        mTextPaint.textAlign = Paint.Align.CENTER

        mTextBounds = Rect()
        mInitial = extractInitial(initial)
        updateTextBounds()

        mBackgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mBackgroundPaint.color = backgroundColor
        mBackgroundPaint.style = Paint.Style.FILL

        mBackgroundBounds = RectF()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        updateCircleDrawBounds(mBackgroundBounds)
    }

    override fun onDraw(canvas: Canvas) {
        if (mShowState == SHOW_INITIAL) {
            val textBottom = mBackgroundBounds.centerY() - mTextBounds.exactCenterY()

            canvas.drawOval(mBackgroundBounds, mBackgroundPaint)
            canvas.drawText(mInitial!!, mBackgroundBounds.centerX(), textBottom, mTextPaint)

            drawStroke(canvas)
            drawHighlight(canvas)
        } else {
            super.onDraw(canvas)
        }
    }

    private fun extractInitial(letter: String?): String {
        return if (letter == null || letter.trim { it <= ' ' }.length <= 0) "?" else letter[0].toString()
    }

    private fun updateTextBounds() {
        mTextPaint.getTextBounds(mInitial, 0, mInitial!!.length, mTextBounds)
    }

    companion object {
        val SHOW_IMAGE = 2
        val SHOW_INITIAL = 1

        private val DEF_TEXT_SIZE = 90
        private val DEF_INITIAL = "A"
        private val DEF_STATE = SHOW_INITIAL
        private val DEF_BACKGROUND_COLOR = 0xE53935
    }
}
