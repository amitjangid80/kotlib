package com.amit.kotlib.shinebtn

import android.animation.Animator
import android.animation.ValueAnimator
import android.app.Activity
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Rect
import android.os.Build
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.animation.LinearInterpolator

import com.amit.kotlib.R

class ShineButton : PorterShapeImageView {
    private var isChecked = false

    private var btnColor: Int = 0
    var color: Int = 0
        private set

    internal var DEFAULT_WIDTH = 50
    internal var DEFAULT_HEIGHT = 50

    internal var metrics: DisplayMetrics? = DisplayMetrics()

    internal var activity: Activity? = null
    internal lateinit var shineView: ShineView
    internal var shakeAnimator: ValueAnimator? = null
    internal var shineParams = ShineView.ShineParams()

    internal var listener: OnCheckedChangeListener? = null

    private var bottomHeight: Int = 0
    private var realBottomHeight: Int = 0

    internal var onButtonClickListener: OnButtonClickListener? = null

    constructor(context: Context) : super(context) {

        if (context is Activity) {
            init(context)
        }
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initButton(context, attrs)
    }


    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initButton(context, attrs)
    }

    private fun initButton(context: Context, attrs: AttributeSet) {
        if (context is Activity) {
            init(context)
        }

        val a = context.obtainStyledAttributes(attrs, R.styleable.ShineButton)
        btnColor = a.getColor(R.styleable.ShineButton_btn_color, Color.GRAY)
        color = a.getColor(R.styleable.ShineButton_btn_fill_color, Color.BLACK)
        shineParams.allowRandomColor = a.getBoolean(R.styleable.ShineButton_allow_random_color, false)
        shineParams.animDuration = a.getInteger(R.styleable.ShineButton_shine_animation_duration, shineParams.animDuration.toInt()).toLong()
        shineParams.bigShineColor = a.getColor(R.styleable.ShineButton_big_shine_color, shineParams.bigShineColor)
        shineParams.clickAnimDuration = a.getInteger(R.styleable.ShineButton_click_animation_duration, shineParams.clickAnimDuration.toInt()).toLong()
        shineParams.enableFlashing = a.getBoolean(R.styleable.ShineButton_enable_flashing, false)
        shineParams.shineCount = a.getInteger(R.styleable.ShineButton_shine_count, shineParams.shineCount)
        shineParams.shineDistanceMultiple = a.getFloat(R.styleable.ShineButton_shine_distance_multiple, shineParams.shineDistanceMultiple)
        shineParams.shineTurnAngle = a.getFloat(R.styleable.ShineButton_shine_turn_angle, shineParams.shineTurnAngle)
        shineParams.smallShineColor = a.getColor(R.styleable.ShineButton_small_shine_color, shineParams.smallShineColor)
        shineParams.smallShineOffsetAngle = a.getFloat(R.styleable.ShineButton_small_shine_offset_angle, shineParams.smallShineOffsetAngle)
        shineParams.shineSize = a.getDimensionPixelSize(R.styleable.ShineButton_shine_size, shineParams.shineSize)
        a.recycle()
        setSrcColor(btnColor)
    }

    fun getBottomHeight(real: Boolean): Int {
        return if (real) {
            realBottomHeight
        } else bottomHeight

    }

    fun isChecked(): Boolean {
        return isChecked
    }


    fun setBtnColor(btnColor: Int) {
        this.btnColor = btnColor
        setSrcColor(this.btnColor)
    }

    fun setBtnFillColor(btnFillColor: Int) {
        this.color = btnFillColor
    }

    fun setChecked(checked: Boolean, anim: Boolean) {
        setChecked(checked, anim, true)
    }

    private fun setChecked(checked: Boolean, anim: Boolean, callBack: Boolean) {
        isChecked = checked

        if (checked) {
            setSrcColor(color)
            isChecked = true

            if (anim) {
                showAnim()
            }
        } else {
            setSrcColor(btnColor)
            isChecked = false

            if (anim) {
                setCancel()
            }
        }

        if (callBack) {
            onListenerUpdate(checked)
        }
    }

    fun setChecked(checked: Boolean) {
        setChecked(checked, false, false)
    }

    private fun onListenerUpdate(checked: Boolean) {
        if (listener != null) {
            listener!!.onCheckedChanged(this, checked)
        }
    }

    fun setCancel() {
        setSrcColor(btnColor)

        if (shakeAnimator != null) {
            shakeAnimator!!.end()
            shakeAnimator!!.cancel()
        }
    }

    fun setAllowRandomColor(allowRandomColor: Boolean) {
        shineParams.allowRandomColor = allowRandomColor
    }

    fun setAnimDuration(durationMs: Int) {
        shineParams.animDuration = durationMs.toLong()
    }

    fun setBigShineColor(color: Int) {
        shineParams.bigShineColor = color
    }

    fun setClickAnimDuration(durationMs: Int) {
        shineParams.clickAnimDuration = durationMs.toLong()
    }

    fun enableFlashing(enable: Boolean) {
        shineParams.enableFlashing = enable
    }

    fun setShineCount(count: Int) {
        shineParams.shineCount = count
    }

    fun setShineDistanceMultiple(multiple: Float) {
        shineParams.shineDistanceMultiple = multiple
    }

    fun setShineTurnAngle(angle: Float) {
        shineParams.shineTurnAngle = angle
    }

    fun setSmallShineColor(color: Int) {
        shineParams.smallShineColor = color
    }

    fun setSmallShineOffAngle(angle: Float) {
        shineParams.smallShineOffsetAngle = angle
    }

    fun setShineSize(size: Int) {
        shineParams.shineSize = size
    }

    override fun setOnClickListener(l: View.OnClickListener?) {
        if (l is OnButtonClickListener) {
            super.setOnClickListener(l)
        } else {
            if (onButtonClickListener != null) {
                onButtonClickListener!!.setListener(l)
            }
        }
    }

    fun setOnCheckStateChangeListener(listener: OnCheckedChangeListener) {
        this.listener = listener
    }

    fun init(activity: Activity) {
        this.activity = activity
        onButtonClickListener = OnButtonClickListener()
        setOnClickListener(onButtonClickListener)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        calPixels()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
    }

    fun showAnim() {
        if (activity != null) {
            val rootView = activity!!.findViewById<View>(Window.ID_ANDROID_CONTENT) as ViewGroup
            shineView = ShineView(activity!!, this, shineParams)
            rootView.addView(shineView, ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
            doShareAnim()
        } else {
            Log.e(TAG, "Please init.")
        }
    }

    fun removeView(view: View) {
        if (activity != null) {
            val rootView = activity!!.findViewById<View>(Window.ID_ANDROID_CONTENT) as ViewGroup
            rootView.removeView(view)
        } else {
            Log.e(TAG, "Please init.")
        }
    }

    fun setShapeResource(raw: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setShape(resources.getDrawable(raw, null))
        } else {
            setShape(resources.getDrawable(raw))
        }
    }

    private fun doShareAnim() {
        shakeAnimator = ValueAnimator.ofFloat(0.4f, 1f, 0.9f, 1f)
        shakeAnimator!!.interpolator = LinearInterpolator()
        shakeAnimator!!.duration = 500
        shakeAnimator!!.startDelay = 180
        invalidate()

        shakeAnimator!!.addUpdateListener { valueAnimator ->
            scaleX = valueAnimator.animatedValue as Float
            scaleY = valueAnimator.animatedValue as Float
        }

        shakeAnimator!!.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animator: Animator) {
                setSrcColor(color)
            }

            override fun onAnimationEnd(animator: Animator) {
                setSrcColor(if (isChecked) color else btnColor)
            }

            override fun onAnimationCancel(animator: Animator) {
                setSrcColor(btnColor)
            }

            override fun onAnimationRepeat(animator: Animator) {

            }
        })

        shakeAnimator!!.start()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    private fun calPixels() {
        if (activity != null && metrics != null) {
            activity!!.windowManager.defaultDisplay.getMetrics(metrics)
            val location = IntArray(2)
            getLocationInWindow(location)

            val visibleFrame = Rect()
            activity!!.window.decorView.getWindowVisibleDisplayFrame(visibleFrame)
            realBottomHeight = visibleFrame.height() - location[1]
            bottomHeight = metrics!!.heightPixels - location[1]
        }
    }

    interface OnCheckedChangeListener {
        fun onCheckedChanged(view: View, checked: Boolean)
    }

    inner class OnButtonClickListener : View.OnClickListener {
        internal var listener: View.OnClickListener? = null

        constructor() {

        }

        constructor(l: View.OnClickListener) {
            listener = l
        }

        fun setListener(listener: View.OnClickListener?) {
            this.listener = listener
        }

        override fun onClick(view: View) {
            if (!isChecked) {
                isChecked = true
                showAnim()
            } else {
                isChecked = false
                setCancel()
            }

            onListenerUpdate(isChecked)

            if (listener != null) {
                listener!!.onClick(view)
            }
        }
    }

    companion object {
        private val TAG = "ShineButton"
    }
}
