package com.amit.kotlib.ui

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.annotation.TargetApi
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.InsetDrawable
import android.graphics.drawable.StateListDrawable
import android.os.Build
import android.os.Handler
import android.support.annotation.ColorRes
import android.support.v4.content.ContextCompat
import android.support.v7.widget.AppCompatButton
import android.util.AttributeSet

import com.amit.kotlib.R
import com.amit.kotlib.drawables.CircularAnimDrawable
import com.amit.kotlib.drawables.CircularRevealAnimDrawable
import com.amit.kotlib.interfaces.AnimBtn
import com.amit.kotlib.interfaces.CustomizeByCode
import com.amit.kotlib.interfaces.OnAnimEndListener
import com.amit.kotlib.utilities.Utils

@Suppress("NAME_SHADOWING")
abstract class ProgressButton : AppCompatButton, AnimBtn, CustomizeByCode {
    private var mGradientDrawable: GradientDrawable? = null
    private var mIsMorphingInProgress: Boolean = false

    private var mState: State? = null
    private var mAnimatedDrawable: CircularAnimDrawable? = null
    private var mRevealDrawable: CircularRevealAnimDrawable? = null
    private var mAnimatorSet: AnimatorSet? = null

    private var mFillColorDone: Int = 0
    private var progress: Int = 0

    private var mBitmapDone: Bitmap? = null
    private var mParams: Params? = null

    private var doneWhileMorphing: Boolean = false
    private var shouldStartAnimation: Boolean = false
    private var layoutDone: Boolean = false

    /**
     * is animating method
     * Check if button is animating
     */
    val isAnimating: Boolean?
        get() = mState == State.PROGRESS

    private enum class State {
        PROGRESS, IDLE, DONE, STOPPED
    }

    /**
     * Constructor of the class
     *
     * @param context - context of the application
     */
    constructor(context: Context) : super(context) {
        init(context, null, 0, 0)
    }

    /**
     * Constructor of the class
     *
     * @param context - context of the application
     * @param attrs - attribute set
     */
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context, attrs, 0, 0)
    }

    /**
     * Constructor of the class
     *
     * @param context - context of the application
     * @param attrs - attribute set
     * @param defStyleAttr - default style attribute
     */
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context, attrs, defStyleAttr, 0)
    }

    /**
     * Constructor of the class
     *
     * @param context - context of the application
     * @param attrs - attribute set
     * @param defStyleAttr - default style attribute
     * @param defStyleRes - default style resources
     */
    @TargetApi(23)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr) {
        init(context, attrs, defStyleAttr, defStyleRes)
    }

    /**
     * Common initializer method.
     *
     * @param context - context of the application
     * @param attrs   Attributes passed in the XML
     */
    private fun init(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) {
        mParams = Params()
        mParams!!.progressBarPadding = 0f
        val drawables: BackgroundAndMorphingDrawables?

        if (attrs == null) {
            drawables = loadGradientDrawable(Utils.getDrawable(getContext(), R.drawable.btn_default_shape))
        } else {
            val attrsArray = intArrayOf(android.R.attr.background)// 0

            val ta = context.obtainStyledAttributes(attrs, R.styleable.ProgressButton, defStyleAttr, defStyleRes)
            val taBg = context.obtainStyledAttributes(attrs, attrsArray, defStyleAttr, defStyleRes)

            drawables = loadGradientDrawable(taBg.getDrawable(0))
            mParams!!.initialCornerRadius = ta.getDimension(R.styleable.ProgressButton_initial_corner_radius, 0f)
            mParams!!.finalCornerRadius = ta.getDimension(R.styleable.ProgressButton_final_corner_radius, 100f)
            mParams!!.progressBarWidth = ta.getDimension(R.styleable.ProgressButton_progress_bar_width, 10f)

            mParams!!.progressBarColor = ta.getColor(R.styleable.ProgressButton_progress_bar_color,
                    Utils.getColorWrapper(context, android.R.color.black))

            mParams!!.progressBarPadding = ta.getDimension(R.styleable.ProgressButton_progress_bar_padding, 0f)
            ta.recycle()
            taBg.recycle()
        }

        mState = State.IDLE
        mParams!!.text = this.text.toString()
        mParams!!.drawables = this.compoundDrawablesRelative

        if (drawables != null) {
            mGradientDrawable = drawables.morphingDrawable

            if (drawables.backGroundDrawable != null) {
                background = drawables.backGroundDrawable
            }
        }

        resetProgress()
    }

    override fun setBackgroundColor(color: Int) {
        mGradientDrawable!!.setColor(color)
    }

    override fun setBackgroundResource(@ColorRes resId: Int) {
        mGradientDrawable!!.setColor(ContextCompat.getColor(context, resId))
    }

    override fun setProgressBarColor(color: Int) {
        mParams!!.progressBarColor = color

        if (mAnimatedDrawable != null) {
            mAnimatedDrawable!!.setLoadingBarColor(color)
        }
    }

    override fun setProgressBarWidth(width: Float) {
        mParams!!.progressBarWidth = width
    }

    override fun setDoneColor(color: Int) {
        mParams!!.doneColor = color
    }

    override fun setProgressBarPadding(padding: Float) {
        mParams!!.progressBarPadding = padding
    }

    override fun setInitialHeight(height: Int) {
        mParams!!.initialHeight = height
    }

    override fun setInitialCornerRadius(radius: Float) {
        mParams!!.initialCornerRadius = radius
    }

    override fun setFinalCornerRadius(radius: Float) {
        mParams!!.finalCornerRadius = radius
    }

    /**
     * on draw method
     * This method is called when the button
     * and its dependencies are going to draw it selves.
     *
     * @param canvas Canvas
     */
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        layoutDone = true

        if (shouldStartAnimation) {
            startAnimation()
        }

        if (mState == State.PROGRESS && !mIsMorphingInProgress) {
            drawProgress(canvas)
        } else if (mState == State.DONE) {
            drawDoneAnimation(canvas)
        }
    }

    /**
     * draw progress method
     * If the mAnimatedDrawable is null or its not running,
     * it get created. Otherwise its draw method is called here.
     *
     * @param canvas Canvas
     */
    private fun drawProgress(canvas: Canvas) {
        if (mAnimatedDrawable == null || !mAnimatedDrawable!!.isRunning()) {
            mAnimatedDrawable = CircularAnimDrawable(this,
                    mParams!!.progressBarWidth,
                    mParams!!.progressBarColor)

            val offset = (width - height) / 2
            val left = offset + mParams!!.progressBarPadding!!.toInt()
            val right = width - offset - mParams!!.progressBarPadding!!.toInt()
            val bottom = height - mParams!!.progressBarPadding!!.toInt()
            val top = mParams!!.progressBarPadding!!.toInt()

            mAnimatedDrawable!!.setBounds(left, top, right, bottom)
            mAnimatedDrawable!!.setCallback(this)
            mAnimatedDrawable!!.start()
        } else {
            mAnimatedDrawable!!.setProgress(progress)
            mAnimatedDrawable!!.draw(canvas)
        }
    }

    /**
     * set progress
     * this method will set the progress.
     *
     * @param progress set a progress to switch displaying a determinate circular progress
     */
    override fun setProgress(progress: Int) {
        var progress: Int = progress
        progress = Math.max(CircularAnimDrawable.MIN_PROGRESS,
                Math.min(CircularAnimDrawable.MAX_PROGRESS, progress))

        this.progress = progress
    }

    /**
     * reset progress method
     * resets a given progress and shows an indeterminate progress animation
     */
    override fun resetProgress() {
        this.progress = CircularAnimDrawable.MIN_PROGRESS - 1
    }

    /**
     * stop animation method
     * Stops the animation and sets the button in the STOPPED state.
     */
    fun stopAnimation() {
        if (mState == State.PROGRESS && !mIsMorphingInProgress) {
            mState = State.STOPPED
            mAnimatedDrawable!!.stop()
        }
    }

    /**
     * on anim completed method
     * Call this method when you want to show a 'completed' or a 'done' status.
     * You have to choose the * color and the image to be shown.
     * If your loading progress ended with a success status you probably
     * want to put a icon for "success" and a green color,
     * otherwise red and a failure icon. You can also
     * show that a music is completed... or show some status on a game... be creative!
     *
     * @param fillColor The color of the background of the button
     * @param bitmap    The image that will be shown
     */
    fun onAnimCompleted(fillColor: Int, bitmap: Bitmap?) {
        if (mState != State.PROGRESS) {
            return
        }

        if (mIsMorphingInProgress) {
            doneWhileMorphing = true
            mFillColorDone = fillColor
            mBitmapDone = bitmap
            return
        }

        mState = State.DONE
        mAnimatedDrawable!!.stop()
        mRevealDrawable = CircularRevealAnimDrawable(this, fillColor, bitmap)

        val left = 0
        val right = width
        val bottom = height
        val top = 0

        // changing the background color of the button.
        this.setBackgroundColor(fillColor)

        // adding elevation to the button
        // if android version is greater or equal to 21 or lollipop
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            this.elevation = 5f
        }

        mRevealDrawable!!.setBounds(left, top, right, bottom)
        mRevealDrawable!!.setCallback(this)
        mRevealDrawable!!.start()
    }

    /**
     * draw done animation
     * Method called on the onDraw when the button is on DONE status
     *
     * @param canvas Canvas
     */
    private fun drawDoneAnimation(canvas: Canvas) {
        mRevealDrawable!!.draw(canvas)
    }

    /**
     * revert animation
     * this method will revert the animation
     * and will bring the button to its original state.
     *
     * @param onAnimationEndListener - listener for what to do after the animation is completed
     */
    @JvmOverloads
    fun revertAnimations(onAnimationEndListener: OnAnimEndListener? = null) {
        if (mState == State.IDLE) {
            return
        }

        mState = State.IDLE
        resetProgress()

        if (mAnimatedDrawable != null && mAnimatedDrawable!!.isRunning()) {
            stopAnimation()
        }

        if (mIsMorphingInProgress) {
            mAnimatorSet!!.cancel()
        }

        isClickable = false
        val fromWidth = width
        val fromHeight = height

        val toHeight = mParams!!.initialHeight
        val toWidth = mParams!!.initialWidth
        var cornerAnimation: ObjectAnimator? = null

        if (mGradientDrawable != null) {
            cornerAnimation = ObjectAnimator.ofFloat(
                    mGradientDrawable,
                    "cornerRadius",
                    mParams!!.finalCornerRadius,
                    mParams!!.initialCornerRadius)
        }

        val widthAnimation = ValueAnimator.ofInt(fromWidth, toWidth)

        widthAnimation.addUpdateListener { valueAnimator ->
            val `val` = valueAnimator.animatedValue as Int
            val layoutParams = layoutParams
            layoutParams.width = `val`
            setLayoutParams(layoutParams)
        }

        val heightAnimation = ValueAnimator.ofInt(fromHeight, toHeight)

        heightAnimation.addUpdateListener { valueAnimator ->
            val `val` = valueAnimator.animatedValue as Int
            val layoutParams = layoutParams
            layoutParams.height = `val`
            setLayoutParams(layoutParams)
        }

        /*ValueAnimator strokeAnimation = ValueAnimator.ofFloat(
                getResources().getDimension(R.dimen.stroke_login_button),
                getResources().getDimension(R.dimen.stroke_login_button_loading));

        strokeAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {

                ((ShapeDrawable)mGradientDrawable).getPaint().setStrokeWidth((Float)animation.getAnimatedValue());
            }
        });*/

        mAnimatorSet = AnimatorSet()
        mAnimatorSet!!.duration = 300

        if (mGradientDrawable != null) {
            mAnimatorSet!!.playTogether(cornerAnimation, widthAnimation, heightAnimation)
        } else {
            mAnimatorSet!!.playTogether(widthAnimation, heightAnimation)
        }

        mAnimatorSet!!.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                isClickable = true
                mIsMorphingInProgress = false
                text = mParams!!.text

                setCompoundDrawablesRelative(
                        mParams!!.drawables!![0],
                        mParams!!.drawables!![1],
                        mParams!!.drawables!![2],
                        mParams!!.drawables!![3])

                if (onAnimationEndListener != null) {
                    onAnimationEndListener.onAnimationEnd()
                }
            }
        })

        mIsMorphingInProgress = true
        mAnimatorSet!!.start()
    }

    override fun dispose() {
        if (mAnimatedDrawable != null) {
            mAnimatedDrawable!!.dispose()
        }

        if (mRevealDrawable != null) {
            mRevealDrawable!!.dispose()
        }
    }

    /**
     * start animation method
     * this method called to start the animation.
     * Morphs in to a ball and then starts a loading spinner.
     */
    override fun startAnimation() {
        if (mState != State.IDLE) {
            return
        }

        if (!layoutDone) {
            shouldStartAnimation = true
            return
        }

        shouldStartAnimation = false

        if (mIsMorphingInProgress) {
            mAnimatorSet!!.cancel()
        } else {
            mParams!!.initialWidth = width
            mParams!!.initialHeight = height
        }

        mState = State.PROGRESS
        mParams!!.text = text.toString()

        this.setCompoundDrawables(null, null, null, null)
        this.text = null
        isClickable = false

        val toHeight = mParams!!.initialHeight

        val cornerAnimation = ObjectAnimator.ofFloat(
                mGradientDrawable,
                "cornerRadius",
                mParams!!.initialCornerRadius,
                mParams!!.finalCornerRadius)

        val widthAnimation = ValueAnimator.ofInt(mParams!!.initialWidth, toHeight)

        widthAnimation.addUpdateListener { valueAnimator ->
            val `val` = valueAnimator.animatedValue as Int
            val layoutParams = layoutParams
            layoutParams.width = `val`
            setLayoutParams(layoutParams)
        }

        val heightAnimation = ValueAnimator.ofInt(mParams!!.initialHeight, toHeight)

        heightAnimation.addUpdateListener { valueAnimator ->
            val `val` = valueAnimator.animatedValue as Int
            val layoutParams = layoutParams
            layoutParams.height = `val`
            setLayoutParams(layoutParams)
        }

        /*ValueAnimator strokeAnimation = ValueAnimator.ofFloat(
                getResources().getDimension(R.dimen.stroke_login_button),
                getResources().getDimension(R.dimen.stroke_login_button_loading));

        strokeAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {

                ((ShapeDrawable)mGradientDrawable).getPaint().setStrokeWidth((Float)animation.getAnimatedValue());
            }
        });*/

        mAnimatorSet = AnimatorSet()
        mAnimatorSet!!.duration = 300
        mAnimatorSet!!.playTogether(cornerAnimation, widthAnimation, heightAnimation)

        mAnimatorSet!!.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                mIsMorphingInProgress = false

                if (doneWhileMorphing) {
                    doneWhileMorphing = false

                    val runnable = Runnable { onAnimCompleted(mFillColorDone, mBitmapDone) }

                    Handler().postDelayed(runnable, 50)
                }
            }
        })

        mIsMorphingInProgress = true
        mAnimatorSet!!.start()
    }

    /**
     * params class
     * Class with all the params to configure the button.
     */
    private inner class Params {
        var progressBarWidth: Float = 0.toFloat()
        var progressBarColor: Int = 0
        var doneColor: Int = 0
        var progressBarPadding: Float? = null
        var initialHeight: Int = 0
        var initialWidth: Int = 0
        var text: String? = null
        var initialCornerRadius: Float = 0.toFloat()
        var finalCornerRadius: Float = 0.toFloat()
        var drawables: Array<Drawable>? = null
    }

    /**
     * background and morphing drawables class
     */
    internal class BackgroundAndMorphingDrawables {
        var backGroundDrawable: Drawable? = null
        var morphingDrawable: GradientDrawable? = null

        fun setBothDrawables(drawable: GradientDrawable) {
            this.backGroundDrawable = drawable
            this.morphingDrawable = drawable
        }
    }

    companion object {

        /**
         * Background And Morphing drawables
         * finds or creates the drawable for the morphing animation
         * and the drawable to set the background to
         *
         * @param drawable Drawable set with android:background setting
         * @return BackgroundAndMorphingDrawables object holding the Drawable to morph and to set a background
         */
        internal fun loadGradientDrawable(drawable: Drawable?): BackgroundAndMorphingDrawables? {
            var mGradientDrawable: BackgroundAndMorphingDrawables? = BackgroundAndMorphingDrawables()

            if (drawable == null) {
                return null
            } else {
                if (drawable is GradientDrawable) {
                    mGradientDrawable!!.setBothDrawables(drawable)
                } else if (drawable is ColorDrawable) {
                    val colorDrawable = drawable as ColorDrawable?
                    val gradientDrawable = GradientDrawable()
                    gradientDrawable.setColor(colorDrawable!!.color)
                    mGradientDrawable!!.setBothDrawables(gradientDrawable)
                } else if (drawable is InsetDrawable && Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    val insetDrawable = drawable as InsetDrawable?
                    mGradientDrawable = loadGradientDrawable(insetDrawable!!.drawable)

                    // use the original inset as background to keep margins,
                    // and use the inner drawable for morphing
                    mGradientDrawable!!.backGroundDrawable = insetDrawable
                } else if (drawable is StateListDrawable) {
                    val stateListDrawable = drawable as StateListDrawable?

                    //try to get the drawable for an active, enabled, unpressed button
                    stateListDrawable!!.state = intArrayOf(android.R.attr.state_enabled, android.R.attr.state_active, -android.R.attr.state_pressed)

                    val current = stateListDrawable.current
                    mGradientDrawable = loadGradientDrawable(current)
                }

                if (mGradientDrawable!!.morphingDrawable == null) {
                    throw RuntimeException("Error reading background... Use a shape or a color in xml!")
                }
            }

            return mGradientDrawable
        }
    }
}
/**
 * revert animation
 * this method will revert the animation
 * and will bring the button to its original state.
 */
