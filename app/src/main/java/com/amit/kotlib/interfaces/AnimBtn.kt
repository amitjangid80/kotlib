package com.amit.kotlib.interfaces

@Suppress("unused")
interface AnimBtn
{
    fun startAnimation()
    fun revertAnimation()
    fun revertAnimation(onAnimEndListener: OnAnimEndListener)
    fun dispose()
    fun setProgress(progress: Int)
    fun resetProgress()
}
