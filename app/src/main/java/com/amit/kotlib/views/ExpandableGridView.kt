package com.amit.kotlib.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.GridView

/**
 * Created by AMIT JANGID on 20-Sep-18.
 *
 * this grid view class can be used instead of normal grid view inside a scroll view
**/
@Suppress("unused")
class ExpandableGridView : GridView
{
    private var isGridViewExpandable = false

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyles: Int) : super(context, attrs, defStyles)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int)
    {
        if (isGridViewExpandable)
        {
            val expandSpec = View.MeasureSpec.makeMeasureSpec(View.MEASURED_SIZE_MASK, View.MeasureSpec.AT_MOST)
            super.onMeasure(widthMeasureSpec, expandSpec)

            val params = layoutParams
            params.height = measuredHeight
        }
        else
        {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        }
    }

    fun setIsGridViewExpandable(isGridViewExpandable: Boolean)
    {
        this.isGridViewExpandable = isGridViewExpandable
    }
}
