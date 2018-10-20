package com.amit.kotlib.iv

import android.content.Context
import android.support.v7.widget.AppCompatImageView
import android.util.AttributeSet

/**
 * Created by AMIT JANGID on 20-Mar-18.
**/
@Suppress("unused")
class SquareImageView : AppCompatImageView
{
    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int)
    {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(measuredWidth, measuredWidth) // Snam to Width
    }
}
