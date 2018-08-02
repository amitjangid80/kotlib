package com.amit.kotlib.socialbtn

import android.content.Context
import android.util.AttributeSet

import com.amit.kotlib.R

class LinkedInButton : BaseButton {
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs, R.color.linked_in, R.drawable.linkedin_logo) {}

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle, R.color.linked_in, R.drawable.linkedin_logo) {}

    constructor(context: Context) : super(context) {}
}
