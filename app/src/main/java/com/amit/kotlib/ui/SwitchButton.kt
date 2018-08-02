package com.amit.kotlib.ui

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.graphics.Typeface
import android.os.Bundle
import android.os.Parcelable
import android.text.TextPaint
import android.text.TextUtils
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

import com.amit.kotlib.R

class SwitchButton @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {
    private var mTabTexts = arrayOf("Male", "Female")
    private var mNumOfTabs = mTabTexts.size

    private var mFillPaint: Paint? = null
    private var mStrokePaint: Paint? = null

    private var mWidth: Int = 0
    private var mHeight: Int = 0

    private var mSelectedTextPaint: TextPaint? = null
    private var mUnSelectedTextPaint: TextPaint? = null
    private var onSwitchListener: OnSwitchListener? = null

    private var perWidth: Float = 0.toFloat()
    private var mTextSize: Float = 0.toFloat()
    private var mStrokeWidth: Float = 0.toFloat()
    private var mStrokeRadius: Float = 0.toFloat()
    private var mTextHeightOffSet: Float = 0.toFloat()

    private var mSelectedTab: Int = 0
    private var mSelectedColor: Int = 0

    private var mTypeFace: String? = null
    private var typeface: Typeface? = null
    private var mFontMetrics: Paint.FontMetrics? = null

    /**
     * get default height when android:layout_height="wrap_content"
     */
    private val defaultHeight: Int
        get() = (mFontMetrics!!.bottom - mFontMetrics!!.top).toInt() + paddingTop + paddingBottom

    /**
     * get default width when android:layout_width="wrap_content"
     */
    private val defaultWidth: Int
        get() {
            var tabTextWidth = 0f
            val tabs = mTabTexts.size

            for (i in 0 until tabs) {
                tabTextWidth = Math.max(tabTextWidth, mSelectedTextPaint!!.measureText(mTabTexts[i]))
            }

            val totalTextWidth = tabTextWidth * tabs
            val totalStrokeWidth = mStrokeWidth * tabs
            val totalPadding = (paddingRight + paddingLeft) * tabs
            return (totalTextWidth + totalStrokeWidth + totalPadding.toFloat()).toInt()
        }

    init {
        initAttrs(context, attrs)
        initPaint()
    }

    /**
     * 2018 May 03 - Thursday - 05:39 PM
     * init attrs method
     *
     * @param context - context of the application
     * @param attrs - attribute set, set by the user
     *
     * this method gets all the attributes defined and used by the user
     */
    private fun initAttrs(context: Context, attrs: AttributeSet?) {
        val a = context.obtainStyledAttributes(attrs, R.styleable.SwitchButton)
        mTypeFace = a.getString(R.styleable.SwitchButton_typeface)
        mTextSize = a.getDimension(R.styleable.SwitchButton_textSize, TEXT_SIZE)
        mSelectedTab = a.getInteger(R.styleable.SwitchButton_selectedTab, SELECTED_TAB)
        mStrokeWidth = a.getDimension(R.styleable.SwitchButton_strokeWidth, STROKE_WIDTH)
        mSelectedColor = a.getColor(R.styleable.SwitchButton_selectedColor, SELECTED_COLOR)
        mStrokeRadius = a.getDimension(R.styleable.SwitchButton_strokeRadius, STROKE_RADIUS)

        val mSwitchTabsResId = a.getResourceId(R.styleable.SwitchButton_switchTabs, 0)

        if (mSwitchTabsResId != 0) {
            mTabTexts = resources.getStringArray(mSwitchTabsResId)
            mNumOfTabs = mTabTexts.size
        }

        if (!TextUtils.isEmpty(mTypeFace)) {
            typeface = Typeface.createFromAsset(context.assets, FONTS_DIR + mTypeFace!!)
        }

        a.recycle()
    }

    private fun initPaint() {
        mStrokePaint = Paint()
        mStrokePaint!!.color = mSelectedColor
        mStrokePaint!!.style = Paint.Style.STROKE
        mStrokePaint!!.strokeWidth = mStrokeWidth
        mStrokePaint!!.isAntiAlias = true

        mFillPaint = Paint()
        mFillPaint!!.color = mSelectedColor
        mFillPaint!!.style = Paint.Style.FILL_AND_STROKE
        mStrokePaint!!.isAntiAlias = true

        mSelectedTextPaint = TextPaint(Paint.ANTI_ALIAS_FLAG)
        mSelectedTextPaint!!.textSize = mTextSize
        mSelectedTextPaint!!.color = -0x1
        mStrokePaint!!.isAntiAlias = true

        mUnSelectedTextPaint = TextPaint(Paint.ANTI_ALIAS_FLAG)
        mUnSelectedTextPaint!!.textSize = mTextSize
        mUnSelectedTextPaint!!.color = mSelectedColor
        mStrokePaint!!.isAntiAlias = true

        mTextHeightOffSet = -(mSelectedTextPaint!!.ascent() + mSelectedTextPaint!!.descent()) * 0.5f
        mFontMetrics = mSelectedTextPaint!!.fontMetrics

        if (typeface != null) {
            mSelectedTextPaint!!.typeface = typeface
            mUnSelectedTextPaint!!.typeface = typeface
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val defaultWidth = defaultWidth
        val defaultHeight = defaultHeight

        setMeasuredDimension(
                getExpectedSize(defaultWidth, widthMeasureSpec),
                getExpectedSize(defaultHeight, heightMeasureSpec))
    }

    /**
     * get expected size method
     *
     * @param size - size of the text
     *
     * @param measureSpec - measure spec
     *
     * @return - returns integer
     */
    private fun getExpectedSize(size: Int, measureSpec: Int): Int {
        var result = size

        val specMode = View.MeasureSpec.getMode(measureSpec)
        val specSize = View.MeasureSpec.getSize(measureSpec)

        when (specMode) {
            View.MeasureSpec.EXACTLY ->

                result = specSize

            View.MeasureSpec.UNSPECIFIED ->

                result = size

            View.MeasureSpec.AT_MOST ->

                result = Math.min(size, specSize)

            else -> {
            }
        }

        return result
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val left = mStrokeWidth * 0.5f
        val top = mStrokeWidth * 0.5f

        val right = mWidth - mStrokeWidth * 0.5f
        val bottom = mHeight - mStrokeWidth * 0.5f

        //draw rounded rectangle
        canvas.drawRoundRect(RectF(left, top, right, bottom), mStrokeRadius, mStrokeRadius, mStrokePaint!!)

        //draw line
        for (i in 0 until mNumOfTabs - 1) {
            canvas.drawLine(perWidth * (i + 1), top, perWidth * (i + 1), bottom, mStrokePaint!!)
        }

        //draw tab and line
        for (i in 0 until mNumOfTabs) {
            val tabText = mTabTexts[i]
            val tabTextWidth = mSelectedTextPaint!!.measureText(tabText)

            if (i == mSelectedTab) {
                //draw selected tab
                if (i == 0) {
                    drawLeftPath(canvas, left, top, bottom)
                } else if (i == mNumOfTabs - 1) {
                    drawRightPath(canvas, top, right, bottom)
                } else {
                    canvas.drawRect(RectF(perWidth * i, top, perWidth * (i + 1), bottom), mFillPaint!!)
                }

                // draw selected text
                canvas.drawText(tabText,
                        0.5f * perWidth * (2 * i + 1).toFloat() - 0.5f * tabTextWidth,
                        mHeight * 0.5f + mTextHeightOffSet, mSelectedTextPaint!!)
            } else {
                //draw unselected text
                canvas.drawText(tabText, 0.5f * perWidth * (2 * i + 1).toFloat() - 0.5f * tabTextWidth, mHeight * 0.5f + mTextHeightOffSet, mUnSelectedTextPaint!!)
            }
        }
    }

    /**
     * draw left path method
     *
     * @param canvas - canvas
     *
     * @param left - left
     *
     * @param top - top
     *
     * @param bottom - bottom
     */
    private fun drawLeftPath(canvas: Canvas, left: Float, top: Float, bottom: Float) {
        val leftPath = Path()
        leftPath.moveTo(left + mStrokeRadius, top)
        leftPath.lineTo(perWidth, top)
        leftPath.lineTo(perWidth, bottom)
        leftPath.lineTo(left + mStrokeRadius, bottom)

        leftPath.arcTo(
                RectF(left, bottom - 2 * mStrokeRadius, left + 2 * mStrokeRadius, bottom),
                90f, 90f)

        leftPath.lineTo(left, top + mStrokeRadius)

        leftPath.arcTo(
                RectF(left, top, left + 2 * mStrokeRadius, top + 2 * mStrokeRadius),
                180f, 90f)

        canvas.drawPath(leftPath, mFillPaint!!)
    }

    /**
     * draw right path
     *
     * @param canvas - canvas
     *
     * @param top - top
     *
     * @param right - right
     *
     * @param bottom - bottom
     */
    private fun drawRightPath(canvas: Canvas, top: Float, right: Float, bottom: Float) {
        val rightPath = Path()
        rightPath.moveTo(right - mStrokeRadius, top)
        rightPath.lineTo(right - perWidth, top)
        rightPath.lineTo(right - perWidth, bottom)
        rightPath.lineTo(right - mStrokeRadius, bottom)

        rightPath.arcTo(
                RectF(right - 2 * mStrokeRadius, bottom - 2 * mStrokeRadius, right, bottom),
                90f, -90f)

        rightPath.lineTo(right, top + mStrokeRadius)

        rightPath.arcTo(
                RectF(right - 2 * mStrokeRadius, top, right, top + 2 * mStrokeRadius),
                0f, -90f)

        canvas.drawPath(rightPath, mFillPaint!!)
    }

    /**
     * called after onMeasure
     *
     * @param w - widht
     *
     * @param h - height
     *
     * @param oldw - old width
     *
     * @param oldh - old height
     */
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        mWidth = measuredWidth
        mHeight = measuredHeight
        perWidth = (mWidth / mNumOfTabs).toFloat()

        checkAttrs()
    }

    /**
     * check attribute where suitable
     */
    private fun checkAttrs() {
        if (mStrokeRadius > 0.5f * mHeight) {
            mStrokeRadius = 0.5f * mHeight
        }
    }

    /**
     * receive the event when touched
     *
     * @param event - event
     * @return true or false
     */
    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_UP) {
            val x = event.x

            for (i in 0 until mNumOfTabs) {
                if (x > perWidth * i && x < perWidth * (i + 1)) {
                    if (mSelectedTab == i) {
                        return true
                    }

                    mSelectedTab = i

                    if (onSwitchListener != null) {
                        onSwitchListener!!.onSwitch(i, mTabTexts[i])
                    }
                }
            }

            invalidate()
        }

        return true
    }

    /**
     * called when switched
     */
    interface OnSwitchListener {
        fun onSwitch(position: Int, tabText: String)
    }

    fun setOnSwitchListener(onSwitchListener: OnSwitchListener): SwitchButton {
        this.onSwitchListener = onSwitchListener
        return this
    }

    /**
     * get position of selected tab
     */
    fun getSelectedTab(): Int {
        return mSelectedTab
    }

    /**
     * set selected tab
     *
     * @param mSelectedTab - selected tab
     * @return - button
     */
    fun setSelectedTab(mSelectedTab: Int): SwitchButton {
        this.mSelectedTab = mSelectedTab
        invalidate()

        if (onSwitchListener != null) {
            onSwitchListener!!.onSwitch(mSelectedTab, mTabTexts[mSelectedTab])
        }

        return this
    }

    fun clearSelection() {
        this.mSelectedTab = -1
        invalidate()
    }

    /**
     * set data for the switch button
     *
     * @param tagTexts -  tag texts
     * @return - button
     */
    fun setText(vararg tagTexts: String): SwitchButton {
        if (tagTexts.size > 1) {
            this.mTabTexts = tagTexts as Array<String>
            mNumOfTabs = tagTexts.size
            requestLayout()
            return this
        } else {
            throw IllegalArgumentException("the size of tagTexts should greater then 1")
        }
    }

    override fun onSaveInstanceState(): Parcelable? {
        val bundle = Bundle()
        bundle.putParcelable("View", super.onSaveInstanceState())
        bundle.putFloat("StrokeRadius", mStrokeRadius)
        bundle.putFloat("StrokeWidth", mStrokeWidth)
        bundle.putFloat("TextSize", mTextSize)
        bundle.putInt("SelectedColor", mSelectedColor)
        bundle.putInt("SelectedTab", mSelectedTab)
        return bundle
    }

    override fun onRestoreInstanceState(state: Parcelable) {
        if (state is Bundle) {
            mStrokeRadius = state.getFloat("StrokeRadius")
            mStrokeWidth = state.getFloat("StrokeWidth")
            mTextSize = state.getFloat("TextSize")
            mSelectedColor = state.getInt("SelectedColor")
            mSelectedTab = state.getInt("SelectedTab")
            super.onRestoreInstanceState(state.getParcelable("View"))
        } else {
            super.onRestoreInstanceState(state)
        }
    }

    companion object {
        private val TAG = SwitchButton::class.java.simpleName

        private val TEXT_SIZE = 50f // changed text size to 30 from 14
        private val STROKE_WIDTH = 2f
        private val STROKE_RADIUS = 0f

        private val SELECTED_TAB = 0
        private val FONTS_DIR = "fonts/"
        private val SELECTED_COLOR = -0x148500
    }
}
