package com.amit.kotlib.anim

import android.animation.TimeInterpolator

/**
 * Created by Amit Jangid on 21,May,2018
**/
class EaseInterpolator(val ease: Ease) : TimeInterpolator
{
    override fun getInterpolation(input: Float): Float
    {
        return EaseProvider.get(this.ease, input)
    }
}
