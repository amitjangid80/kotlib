package com.amit.kotlib.socialbtn

import android.content.Context
import android.util.AttributeSet

import com.amit.kotlib.R

class TwitterButton : BaseButton {
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs, R.color.twitter, R.drawable.twitter_logo) {}

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle, R.color.twitter, R.drawable.twitter_logo) {}

    constructor(context: Context) : super(context) {}
}
