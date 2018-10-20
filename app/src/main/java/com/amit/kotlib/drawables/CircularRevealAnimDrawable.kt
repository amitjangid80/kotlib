package com.amit.kotlib.drawables

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.graphics.*
import android.graphics.drawable.Animatable
import android.graphics.drawable.Drawable
import android.view.View
import android.view.animation.DecelerateInterpolator

/**
 * Created by Leandro Ferreira on 22/09/16.
 *
 *
 * An animation of the 'Done' status. It makes a circular reveal of a background color and the
 * than show the image passed in the constructor.
**/
class CircularRevealAnimDrawable
/**
 * @param mAnimatedView      The view that if being animated
 * @param fillColor The color of the background that will the revealed
 * @param mReadyImage    The animage that will be shown in the end of the animation.
**/
(private val mAnimatedView: View, fillColor: Int, private var mReadyImage: Bitmap?) : Drawable(), Animatable
{
    var isFilled: Boolean = false
        private set

    private val mPaint: Paint
    private val mPaintImageReady: Paint
    private var mRadius: Float = 0.toFloat()
    private var mFinalRadius: Float = 0.toFloat()
    private var mRevealInAnimation: ValueAnimator? = null
    private var isRunning: Boolean = false
    private var mCenterWidth: Float = 0.toFloat()
    private var mCenterHeight: Float = 0.toFloat()
    private var mImageReadyAlpha: Int = 0
    private var bitMapXOffset: Float = 0.toFloat()
    private var bitMapYOffset: Float = 0.toFloat()

    init
    {
        isRunning = false

        mPaint = Paint()
        mPaint.isAntiAlias = true
        mPaint.style = Paint.Style.FILL
        mPaint.color = fillColor

        mPaintImageReady = Paint()
        mPaintImageReady.isAntiAlias = true
        mPaintImageReady.style = Paint.Style.FILL
        mPaintImageReady.color = Color.TRANSPARENT
        mImageReadyAlpha = 0
        mRadius = 0f
    }

    /**
     * The method is called when bounds change
     *
     * @param bounds
    **/
    override fun onBoundsChange(bounds: Rect)
    {
        super.onBoundsChange(bounds)

        val bitMapWidth = ((bounds.right - bounds.left) * 0.6).toInt()
        val bitMapHeight = ((bounds.bottom - bounds.top) * 0.6).toInt()

        bitMapXOffset = ((bounds.right - bounds.left - bitMapWidth) / 2).toFloat()
        bitMapYOffset = ((bounds.bottom - bounds.top - bitMapHeight) / 2).toFloat()

        mReadyImage = Bitmap.createScaledBitmap(mReadyImage!!, bitMapWidth, bitMapHeight, false)

        mFinalRadius = ((bounds.right - bounds.left) / 2).toFloat()
        mCenterWidth = ((bounds.right + bounds.left) / 2).toFloat()
        mCenterHeight = ((bounds.bottom + bounds.top) / 2).toFloat()
    }

    /**
     * Setup all the animations. There are a reveal animation to show the button background
     * and a alpha animation to show the bitmap.
    **/
    private fun setupAnimations()
    {
        val alphaAnimator = ValueAnimator.ofInt(0, 255)
        alphaAnimator.duration = 80

        alphaAnimator.addUpdateListener { animation ->
            mImageReadyAlpha = animation.animatedValue as Int
            invalidateSelf()
            mAnimatedView.invalidate()
        }

        mRevealInAnimation = ValueAnimator.ofFloat(0F, mFinalRadius)
        mRevealInAnimation!!.interpolator = DecelerateInterpolator()
        mRevealInAnimation!!.duration = 120

        mRevealInAnimation!!.addUpdateListener { animation ->
            mRadius = animation.animatedValue as Float
            invalidateSelf()
            mAnimatedView.invalidate()
        }

        mRevealInAnimation!!.addListener(object : AnimatorListenerAdapter()
        {
            override fun onAnimationEnd(animation: Animator)
            {
                super.onAnimationEnd(animation)
                isFilled = true
                alphaAnimator.start()
            }
        })
    }

    /**
     * Starts the animation
    **/
    override fun start()
    {
        if (isRunning())
        {
            return
        }

        setupAnimations()
        isRunning = true
        mRevealInAnimation!!.start()
    }

    /**
     * Stops the animation
    **/
    override fun stop()
    {
        if (!isRunning())
        {
            return
        }

        isRunning = false
        mRevealInAnimation!!.cancel()
    }

    /**
     * @return Return if its running or not.
    **/
    override fun isRunning(): Boolean
    {
        return isRunning
    }

    override fun draw(canvas: Canvas)
    {
        canvas.drawCircle(mCenterWidth, mCenterHeight, mRadius, mPaint)

        if (isFilled)
        {
            mPaintImageReady.alpha = mImageReadyAlpha
            canvas.drawBitmap(mReadyImage!!, bitMapXOffset, bitMapYOffset, mPaintImageReady)
        }
    }

    override fun setAlpha(alpha: Int)
    {

    }

    override fun setColorFilter(colorFilter: ColorFilter?)
    {

    }

    override fun getOpacity(): Int
    {
        return PixelFormat.OPAQUE
    }

    fun dispose()
    {
        if (mRevealInAnimation != null)
        {
            mRevealInAnimation!!.end()
            mRevealInAnimation!!.removeAllUpdateListeners()
            mRevealInAnimation!!.cancel()
        }

        mRevealInAnimation = null
    }
}
