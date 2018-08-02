package com.amit.kotlib.socialbtn

import android.content.Context
import android.util.AttributeSet

import com.amit.kotlib.R

class FacebookButton : BaseButton {
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs, R.color.facebook, R.drawable.fb_logo) {}

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle, R.color.facebook, R.drawable.fb_logo) {}

    constructor(context: Context) : super(context) {}
}
