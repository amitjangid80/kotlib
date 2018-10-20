package com.amit.kotlib.iv

import android.content.Context
import android.graphics.Matrix
import android.graphics.PointF
import android.support.v7.widget.AppCompatImageView
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import android.widget.ImageView

/**
 * Created by AMIT JANGID
 * 2018 April 17 - Tuesday - 01:08 PM
 *
 * this class can be used for setting zoom in or zoom out on the image view
**/
@Suppress("unused", "UNUSED_ANONYMOUS_PARAMETER", "MemberVisibilityCanBePrivate")
class TouchImageView : AppCompatImageView
{
    private var matrix1: Matrix? = null
    private var mode = NONE

    // Remember some things for zooming
    private val last = PointF()
    private val start = PointF()

    private val minScale = .1f
    private var maxScale = 3f

    private var m: FloatArray? = null
    private var saveScale = 1f
    private var viewWidth: Int = 0
    private var viewHeight: Int = 0

    private var origWidth: Float = 0.toFloat()
    private var origHeight: Float = 0.toFloat()
    private var oldMeasuredWidth: Int = 0
    private var oldMeasuredHeight: Int = 0

    private var mScaleDetector: ScaleGestureDetector? = null
    // private Context context;

    constructor(context: Context) : super(context)
    {
        sharedConstructing(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    {
        sharedConstructing(context)
    }

    private fun sharedConstructing(context: Context)
    {
        super.setClickable(true)

        // this.context = context;
        mScaleDetector = ScaleGestureDetector(context, ScaleListener())

        matrix1 = Matrix()
        m = FloatArray(9)
        imageMatrix = matrix1
        scaleType = ImageView.ScaleType.MATRIX

        setOnTouchListener { v, event ->
            mScaleDetector!!.onTouchEvent(event)
            val curr = PointF(event.x, event.y)

            when (event.action)
            {
                MotionEvent.ACTION_DOWN ->
                {
                    last.set(curr)
                    start.set(last)
                    mode = DRAG
                }

                MotionEvent.ACTION_MOVE ->

                    if (mode == DRAG)
                    {
                        val deltaX = curr.x - last.x
                        val deltaY = curr.y - last.y

                        val fixTransX = getFixDragTrans(deltaX, viewWidth.toFloat(), origWidth * saveScale)
                        val fixTransY = getFixDragTrans(deltaY, viewHeight.toFloat(), origHeight * saveScale)
                        matrix1!!.postTranslate(fixTransX, fixTransY)

                        fixTrans()
                        last.set(curr.x, curr.y)
                    }

                MotionEvent.ACTION_UP ->
                {
                    mode = NONE
                    val xDiff = Math.abs(curr.x - start.x).toInt()
                    val yDiff = Math.abs(curr.y - start.y).toInt()

                    if (xDiff < CLICK && yDiff < CLICK)
                    {
                        performClick()
                    }
                }

                MotionEvent.ACTION_POINTER_UP ->

                    mode = NONE
            }

            imageMatrix = matrix1
            invalidate()
            true // indicate event was handled
        }
    }

    fun setMaxZoom(x: Float)
    {
        maxScale = x
    }

    private inner class ScaleListener : ScaleGestureDetector.SimpleOnScaleGestureListener()
    {
        override fun onScaleBegin(detector: ScaleGestureDetector): Boolean
        {
            mode = ZOOM
            return true
        }

        override fun onScale(detector: ScaleGestureDetector): Boolean
        {
            var mScaleFactor = detector.scaleFactor
            val origScale = saveScale
            saveScale *= mScaleFactor

            if (saveScale > maxScale)
            {
                saveScale = maxScale
                mScaleFactor = maxScale / origScale
            }
            else if (saveScale < minScale)
            {
                saveScale = minScale
                mScaleFactor = minScale / origScale
            }

            if (origWidth * saveScale <= viewWidth || origHeight * saveScale <= viewHeight)
            {
                matrix1!!.postScale(mScaleFactor, mScaleFactor, (viewWidth / 2).toFloat(), (viewHeight / 2).toFloat())
            }
            else
            {
                matrix1!!.postScale(mScaleFactor, mScaleFactor, detector.focusX, detector.focusY)
            }

            fixTrans()
            return true
        }
    }

    internal fun fixTrans()
    {
        matrix1!!.getValues(m)

        val transX = m!![Matrix.MTRANS_X]
        val transY = m!![Matrix.MTRANS_Y]

        val fixTransX = getFixTrans(transX, viewWidth.toFloat(), origWidth * saveScale)
        val fixTransY = getFixTrans(transY, viewHeight.toFloat(), origHeight * saveScale)

        if (fixTransX != 0f || fixTransY != 0f)
        {
            matrix1!!.postTranslate(fixTransX, fixTransY)
        }
    }

    internal fun getFixTrans(trans: Float, viewSize: Float, contentSize: Float): Float
    {
        val minTrans: Float
        val maxTrans: Float

        if (contentSize <= viewSize)
        {
            minTrans = 0f
            maxTrans = viewSize - contentSize
        }
        else
        {
            minTrans = viewSize - contentSize
            maxTrans = 0f
        }

        if (trans < minTrans)
        {
            return -trans + minTrans
        }

        return if (trans > maxTrans)
        {
            -trans + maxTrans
        }
        else 0f
    }

    internal fun getFixDragTrans(delta: Float, viewSize: Float, contentSize: Float): Float
    {
        return if (contentSize <= viewSize)
        {
            0f
        } else delta

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int)
    {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        viewWidth = View.MeasureSpec.getSize(widthMeasureSpec)
        viewHeight = View.MeasureSpec.getSize(heightMeasureSpec)

        // Rescales image on rotation
        if (oldMeasuredHeight == viewWidth && oldMeasuredHeight == viewHeight || viewWidth == 0 || viewHeight == 0)
        {
            return
        }

        oldMeasuredHeight = viewHeight
        oldMeasuredWidth = viewWidth

        if (saveScale == 1f)
        {
            //Fit to screen.
            val scale: Float
            val drawable = drawable

            if (drawable == null || drawable.intrinsicWidth == 0 || drawable.intrinsicHeight == 0)
            {
                return
            }

            val bmWidth = drawable.intrinsicWidth
            val bmHeight = drawable.intrinsicHeight
            Log.d("bmSize", "bmWidth: $bmWidth bmHeight : $bmHeight")

            val scaleX = viewWidth.toFloat() / bmWidth.toFloat()
            val scaleY = viewHeight.toFloat() / bmHeight.toFloat()

            scale = Math.min(scaleX, scaleY)
            matrix1!!.setScale(scale, scale)

            // Center the image
            var redundantYSpace = viewHeight.toFloat() - scale * bmHeight.toFloat()
            var redundantXSpace = viewWidth.toFloat() - scale * bmWidth.toFloat()

            redundantYSpace /= 2.toFloat()
            redundantXSpace /= 2.toFloat()

            matrix1!!.postTranslate(redundantXSpace, redundantYSpace)
            origWidth = viewWidth - 2 * redundantXSpace
            origHeight = viewHeight - 2 * redundantYSpace
            imageMatrix = matrix1
        }

        fixTrans()
    }

    companion object
    {
        // We can be in one of these 3 states
        private const val NONE = 0
        private const val DRAG = 1
        private const val ZOOM = 4
        private const val CLICK = 3
    }
}
