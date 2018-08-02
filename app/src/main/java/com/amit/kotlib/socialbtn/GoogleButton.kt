package com.amit.kotlib.socialbtn

import android.content.Context
import android.util.AttributeSet

import com.amit.kotlib.R

class GoogleButton : BaseButton {
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs, R.color.google, R.drawable.google_logo) {}

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle, R.color.google, R.drawable.google_logo) {}

    constructor(context: Context) : super(context) {}
}
