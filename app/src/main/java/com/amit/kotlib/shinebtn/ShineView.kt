package com.amit.kotlib.shinebtn

import android.animation.Animator
import android.animation.ValueAnimator
import android.app.Activity
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import android.view.WindowManager

import com.amit.kotlib.anim.Ease
import com.amit.kotlib.anim.EaseInterpolator

import java.util.Random

class ShineView : View {

    internal var shineAnimator: ShineAnimator? = null
    internal lateinit var clickAnimator: ValueAnimator

    internal lateinit var shineButton: ShineButton
    private var paint: Paint? = null
    private var paint2: Paint? = null
    private var paintSmall: Paint? = null

    internal var colorCount = 10

    //Customer property
    internal var smallOffsetAngle: Float = 0.toFloat()
    internal var turnAngle: Float = 0.toFloat()
    internal var shineDistanceMultiple: Float = 0.toFloat()

    internal var animDuration: Long = 0
    internal var clickAnimDuration: Long = 0

    internal var shineCount: Int = 0
    internal var shineSize = 0
    internal var smallShineColor = colorRandom[0]
    internal var bigShineColor = colorRandom[1]

    internal var allowRandomColor = false
    internal var enableFlashing = false

    internal var rectF = RectF()
    internal var rectFSmall = RectF()

    internal var random = Random()
    internal var centerAnimX: Int = 0
    internal var centerAnimY: Int = 0
    internal var btnWidth: Int = 0
    internal var btnHeight: Int = 0

    internal var thirdLength: Double = 0.toDouble()
    internal var value: Float = 0.toFloat()
    internal var clickValue = 0f
    internal var isRun = false
    private val distanceOffset = 0.2f

    constructor(context: Context) : super(context) {}

    constructor(context: Context, shineButton: ShineButton, shineParams: ShineParams) : super(context) {
        initShineParams(shineParams, shineButton)

        this.shineAnimator = ShineAnimator(animDuration, shineDistanceMultiple, clickAnimDuration)
        ValueAnimator.setFrameDelay(FRAME_REFRESH_DELAY)
        this.shineButton = shineButton

        paint = Paint()
        paint!!.color = bigShineColor
        paint!!.strokeWidth = 20f
        paint!!.style = Paint.Style.STROKE
        paint!!.strokeCap = Paint.Cap.ROUND

        paint2 = Paint()
        paint2!!.color = Color.WHITE
        paint2!!.strokeWidth = 20f
        paint2!!.strokeCap = Paint.Cap.ROUND

        paintSmall = Paint()
        paintSmall!!.color = smallShineColor
        paintSmall!!.strokeWidth = 10f
        paintSmall!!.style = Paint.Style.STROKE
        paintSmall!!.strokeCap = Paint.Cap.ROUND

        clickAnimator = ValueAnimator.ofFloat(0f, 1.1f)
        ValueAnimator.setFrameDelay(FRAME_REFRESH_DELAY)
        clickAnimator.duration = clickAnimDuration
        clickAnimator.interpolator = EaseInterpolator(Ease.QUART_OUT)

        clickAnimator.addUpdateListener { valueAnimator ->
            clickValue = valueAnimator.animatedValue as Float
            invalidate()
        }

        clickAnimator.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animator: Animator) {

            }

            override fun onAnimationEnd(animator: Animator) {
                clickValue = 0f
                invalidate()
            }

            override fun onAnimationCancel(animator: Animator) {

            }

            override fun onAnimationRepeat(animator: Animator) {

            }
        })

        shineAnimator!!.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animator: Animator) {

            }

            override fun onAnimationEnd(animator: Animator) {
                shineButton.removeView(this@ShineView)
            }

            override fun onAnimationCancel(animator: Animator) {

            }

            override fun onAnimationRepeat(animator: Animator) {

            }
        })
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}

    fun showAnimation(shineButton: ShineButton) {
        btnWidth = shineButton.width
        btnHeight = shineButton.height
        thirdLength = getThirdLength(btnHeight, btnWidth)
        val location = IntArray(2)

        shineButton.getLocationInWindow(location)
        val visibleFrame = Rect()

        if (isWindowsNotLimit(shineButton.activity!!)) {
            shineButton.activity!!.getWindow().getDecorView().getLocalVisibleRect(visibleFrame)
        } else {
            shineButton.activity!!.getWindow().getDecorView().getWindowVisibleDisplayFrame(visibleFrame)
        }

        centerAnimX = location[0] + btnWidth / 2 - visibleFrame.left // If navigation bar is not displayed on left, visibleFrame.left is 0.

        if (isTranslucentNavigation(shineButton.activity!!)) {
            if (isFullScreen(shineButton.activity!!)) {
                centerAnimY = visibleFrame.height() - shineButton.getBottomHeight(false) + btnHeight / 2
            } else {
                centerAnimY = visibleFrame.height() - shineButton.getBottomHeight(true) + btnHeight / 2
            }
        } else {
            centerAnimY = measuredHeight - shineButton.getBottomHeight(false) + btnHeight / 2
        }

        shineAnimator!!.addUpdateListener { valueAnimator ->
            value = valueAnimator.animatedValue as Float

            if (shineSize != 0 && shineSize > 0) {
                paint!!.strokeWidth = shineSize * (shineDistanceMultiple - value)
                paintSmall!!.strokeWidth = shineSize.toFloat() / 3 * 2 * (shineDistanceMultiple - value)
            } else {
                paint!!.strokeWidth = btnWidth / 2 * (shineDistanceMultiple - value)
                paintSmall!!.strokeWidth = btnWidth / 3 * (shineDistanceMultiple - value)
            }

            rectF.set(centerAnimX - btnWidth / (3 - shineDistanceMultiple) * value,
                    centerAnimY - btnHeight / (3 - shineDistanceMultiple) * value,
                    centerAnimX + btnWidth / (3 - shineDistanceMultiple) * value,
                    centerAnimY + btnHeight / (3 - shineDistanceMultiple) * value)

            rectFSmall.set(centerAnimX - btnWidth / (3 - shineDistanceMultiple + distanceOffset) * value,
                    centerAnimY - btnHeight / (3 - shineDistanceMultiple + distanceOffset) * value,
                    centerAnimX + btnWidth / (3 - shineDistanceMultiple + distanceOffset) * value,
                    centerAnimY + btnHeight / (3 - shineDistanceMultiple + distanceOffset) * value)

            invalidate()
        }

        shineAnimator!!.startAnim(this, centerAnimX, centerAnimY)
        clickAnimator.start()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        for (i in 0 until shineCount) {
            if (allowRandomColor) {
                paint!!.color = colorRandom[if (Math.abs(colorCount / 2 - i) >= colorCount) colorCount - 1 else Math.abs(colorCount / 2 - i)]
            }

            canvas.drawArc(rectF,
                    360f / shineCount * i + 1f + (value - 1) * turnAngle,
                    0.1f,
                    false,
                    getConfigPaint(paint!!))
        }

        for (i in 0 until shineCount) {
            if (allowRandomColor) {
                paint!!.color = colorRandom[if (Math.abs(colorCount / 2 - i) >= colorCount) colorCount - 1 else Math.abs(colorCount / 2 - i)]
            }

            canvas.drawArc(rectFSmall,
                    360f / shineCount * i + 1 - smallOffsetAngle + (value - 1) * turnAngle,
                    0.1f,
                    false,
                    getConfigPaint(paintSmall!!))

        }

        paint!!.strokeWidth = btnWidth.toFloat() * clickValue * (shineDistanceMultiple - distanceOffset)

        if (clickValue != 0f) {
            paint2!!.strokeWidth = btnWidth.toFloat() * clickValue * (shineDistanceMultiple - distanceOffset) - 8
        } else {
            paint2!!.strokeWidth = 0f
        }

        canvas.drawPoint(centerAnimX.toFloat(), centerAnimY.toFloat(), paint)
        canvas.drawPoint(centerAnimX.toFloat(), centerAnimY.toFloat(), paint2)

        if (shineAnimator != null && !isRun) {
            isRun = true
            showAnimation(shineButton)
        }
    }

    private fun getConfigPaint(paint: Paint): Paint {
        if (enableFlashing) {
            paint.color = colorRandom[random.nextInt(colorCount - 1)]
        }

        return paint
    }

    private fun getThirdLength(btnHeight: Int, btnWidth: Int): Double {
        val all = btnHeight * btnHeight + btnWidth * btnWidth
        return Math.sqrt(all.toDouble())
    }

    class ShineParams internal constructor() {

        var allowRandomColor = false
        var animDuration: Long = 1500
        var bigShineColor = 0
        var clickAnimDuration: Long = 200
        var enableFlashing = false
        var shineCount = 7
        var shineTurnAngle = 20f
        var shineDistanceMultiple = 1.5f
        var smallShineOffsetAngle = 20f
        var smallShineColor = 0
        var shineSize = 0

        init {
            colorRandom[0] = Color.parseColor("#FFFF99")
            colorRandom[1] = Color.parseColor("#FFCCCC")
            colorRandom[2] = Color.parseColor("#996699")
            colorRandom[3] = Color.parseColor("#FF6666")
            colorRandom[4] = Color.parseColor("#FFFF66")
            colorRandom[5] = Color.parseColor("#F44336")
            colorRandom[6] = Color.parseColor("#666666")
            colorRandom[7] = Color.parseColor("#CCCC00")
            colorRandom[8] = Color.parseColor("#666666")
            colorRandom[9] = Color.parseColor("#999933")
        }
    }

    private fun initShineParams(shineParams: ShineParams, shineButton: ShineButton) {
        shineCount = shineParams.shineCount
        turnAngle = shineParams.shineTurnAngle
        smallOffsetAngle = shineParams.smallShineOffsetAngle
        enableFlashing = shineParams.enableFlashing
        allowRandomColor = shineParams.allowRandomColor
        shineDistanceMultiple = shineParams.shineDistanceMultiple
        animDuration = shineParams.animDuration
        clickAnimDuration = shineParams.clickAnimDuration
        smallShineColor = shineParams.smallShineColor
        bigShineColor = shineParams.bigShineColor
        shineSize = shineParams.shineSize

        if (smallShineColor == 0) {
            smallShineColor = colorRandom[6]
        }

        if (bigShineColor == 0) {
            bigShineColor = shineButton.color
        }
    }

    private fun isWindowsNotLimit(activity: Activity): Boolean {
        val flag = activity.window.attributes.flags
        return flag and WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS == WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS

        /*if ((flag & WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
                == WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        {
            return true;
        }
        else
        {
            return false;
        }*/
    }

    companion object {
        private val TAG = "ShineView"
        private val FRAME_REFRESH_DELAY: Long = 25 //default 10ms ,change to 25ms for saving cpu.
        internal var colorRandom = IntArray(10)

        /**
         * @param activity
         * @return isFullScreen
         */
        fun isFullScreen(activity: Activity): Boolean {
            val flag = activity.window.attributes.flags
            return flag and WindowManager.LayoutParams.FLAG_FULLSCREEN == WindowManager.LayoutParams.FLAG_FULLSCREEN

            /*if ((flag & WindowManager.LayoutParams.FLAG_FULLSCREEN)
                == WindowManager.LayoutParams.FLAG_FULLSCREEN)
        {
            return true;
        }
        else
        {
            return false;
        }*/
        }

        /**
         * @param activity
         * @return isTranslucentNavigation
         */
        fun isTranslucentNavigation(activity: Activity): Boolean {
            val flag = activity.window.attributes.flags
            return flag and WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION == WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION

            /*if ((flag & WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
                == WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
        {
            return true;
        }
        else
        {
            return false;
        }*/
        }
    }
}
