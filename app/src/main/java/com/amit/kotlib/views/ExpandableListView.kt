package com.amit.kotlib.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ListView

/**
 * Created by AMIT JANGID on 02-Oct-18.
 *
 * this class is used for showing data in list view inside a scroll view
**/
@Suppress("unused")
class ExpandableListView : ListView
{
    private var isListViewExpandable = false

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int)
    {
        if (isListViewExpandable)
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

    fun setIsListViewExpandable(isListViewExpandable: Boolean)
    {
        this.isListViewExpandable = isListViewExpandable
    }
}
