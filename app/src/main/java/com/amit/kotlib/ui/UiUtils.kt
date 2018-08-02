package com.amit.kotlib.ui

import android.support.design.widget.TextInputEditText
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import android.widget.TextView

object UiUtils {
    private val TAG = UiUtils::class.java.simpleName

    /**
     * set char counter
     * Shows live character counter for the number of characters
     * typed in the parameter [EditText]
     *
     * @param editText          Characters to count from
     * @param tvCounterView     [TextView] to show live character count in
     * @param maxCharCount      Max characters that can be typed in into the parameter edit text
     * @param countDown         if true, only the remaining of the max character count will be displayed.
     * if false, current character count as well as max character count will be displayed in the UI.
     */
    fun setCharCounter(editText: EditText?,
                       tvCounterView: TextView?,
                       maxCharCount: Int,
                       countDown: Boolean) {
        try {
            if (editText == null) {
                throw NullPointerException("EditText to count text on cannot be null.")
            }

            if (tvCounterView == null) {
                throw NullPointerException("TextView on which counter should be displayed cannot be null.")
            }

            if (countDown) {
                tvCounterView.text = maxCharCount.toString()
            } else {
                val counterView = "0 / $maxCharCount"
                tvCounterView.text = counterView
            }

            // setting max length to edit text
            setMaxLength(editText, maxCharCount)

            editText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

                }

                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    if (countDown) {
                        // show only the remaining number of characters
                        val charsLeft = maxCharCount - s.length

                        if (charsLeft >= 0) {
                            tvCounterView.text = charsLeft.toString()
                        }
                    } else {
                        // show number of chars / max chars in the UI
                        val counter = s.length.toString() + " / " + maxCharCount
                        tvCounterView.text = counter
                    }
                }

                override fun afterTextChanged(s: Editable) {

                }
            })
        } catch (e: Exception) {
            Log.e(TAG, "setCharCounter: exception while setting char counter.")
            e.printStackTrace()
        }

    }

    /**
     * set char counter
     * Shows live character counter for the number of characters
     *
     * @param textInputEditText          Characters to count from
     * @param tvCounterView     [TextView] to show live character count in
     * @param maxCharCount      Max characters that can be typed in into the parameter edit text
     * @param countDown         if true, only the remaining of the max character count will be displayed.
     * if false, current character count as well as max character count will be displayed in the UI.
     */
    fun setCharCounter(textInputEditText: TextInputEditText?,
                       tvCounterView: TextView?,
                       maxCharCount: Int,
                       countDown: Boolean) {
        try {
            if (textInputEditText == null) {
                throw NullPointerException("EditText to count text on cannot be null.")
            }

            if (tvCounterView == null) {
                throw NullPointerException("TextView on which counter should be displayed cannot be null.")
            }

            if (countDown) {
                tvCounterView.text = maxCharCount.toString()
            } else {
                val counterView = "0 / $maxCharCount"
                tvCounterView.text = counterView
            }

            // setting max length to text input edit text
            setMaxLength(textInputEditText, maxCharCount)

            textInputEditText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

                }

                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    if (countDown) {
                        // show only the remaining number of characters
                        val charsLeft = maxCharCount - s.length

                        if (charsLeft >= 0) {
                            tvCounterView.text = charsLeft.toString()
                        }
                    } else {
                        // show number of chars / max chars in the UI
                        val counter = s.length.toString() + " / " + maxCharCount
                        tvCounterView.text = counter
                    }
                }

                override fun afterTextChanged(s: Editable) {

                }
            })
        } catch (e: Exception) {
            Log.e(TAG, "setCharCounter: exception while setting char counter.")
            e.printStackTrace()
        }

    }

    /**
     * set max length
     * this method sets max text length for text view
     *
     * @param textView - text view on which you want to set max length
     * @param maxLength - length to set on text view
     */
    fun setMaxLength(textView: TextView?, maxLength: Int) {
        try {
            if (textView == null) {
                throw NullPointerException("TextView cannot be null.")
            }

            val fArray = arrayOfNulls<InputFilter>(1)
            fArray[0] = InputFilter.LengthFilter(maxLength)
            textView.filters = fArray
        } catch (e: Exception) {
            Log.e(TAG, "setMaxLength: exception while setting max length to text view.")
            e.printStackTrace()
        }

    }

    /**
     * set max length
     * this method sets max text length for text view
     *
     * @param editText - text view on which you want to set max length
     * @param maxLength - length to set on text view
     */
    fun setMaxLength(editText: EditText?, maxLength: Int) {
        try {
            if (editText == null) {
                throw NullPointerException("TextView cannot be null.")
            }

            val fArray = arrayOfNulls<InputFilter>(1)
            fArray[0] = InputFilter.LengthFilter(maxLength)
            editText.filters = fArray
        } catch (e: Exception) {
            Log.e(TAG, "setMaxLength: exception while setting max length to text view.")
            e.printStackTrace()
        }
    }

    /**
     * set max length
     * this method sets max text length for text view
     *
     * @param textInputEditText - text view on which you want to set max length
     * @param maxLength - length to set on text view
     */
    fun setMaxLength(textInputEditText: TextInputEditText?, maxLength: Int) {
        try {
            if (textInputEditText == null) {
                throw NullPointerException("TextView cannot be null.")
            }

            val fArray = arrayOfNulls<InputFilter>(1)
            fArray[0] = InputFilter.LengthFilter(maxLength)
            textInputEditText.filters = fArray
        } catch (e: Exception) {
            Log.e(TAG, "setMaxLength: exception while setting max length to text view.")
            e.printStackTrace()
        }

    }
}
