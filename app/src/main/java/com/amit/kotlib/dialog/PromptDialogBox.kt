package com.amit.kotlib.dialog

import android.app.Dialog
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.RoundRectShape
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationSet
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView

import com.amit.kotlib.R
import com.amit.kotlib.anim.AnimLoader
import com.amit.kotlib.utilities.Utils

/**
 * Created by Amit Jangid on 22,May,2018
 */
class PromptDialogBox @JvmOverloads constructor(context: Context, theme: Int = 0) : Dialog(context, R.style.PromptDialogTheme) {
    private var mAnimIn: AnimationSet? = null
    private var mAnimOut: AnimationSet? = null

    private var mDialogView: View? = null
    var titleTextView: TextView? = null
        private set
    var contentTextView: TextView? = null
        private set
    private var mPositiveBtn: TextView? = null
    private var mOnPositiveListener: onPositiveListener? = null

    private var mDialogType: Int = 0
    private var mIsShowAnim: Boolean = false
    private var mTitle: CharSequence? = null
    private var mContent: CharSequence? = null
    private var mBtnText: CharSequence? = null

    init {
        init()
    }

    private fun init() {
        mAnimIn = AnimLoader.getInAnimation(context)
        mAnimOut = AnimLoader.getOutAnimation(context)
    }

    override fun onCreate(savedInstanceState: Bundle) {
        super.onCreate(savedInstanceState)

        initView()
        initListener()
    }

    private fun initView() {
        val contentView = View.inflate(context, R.layout.layout_prompt_dialog, null)
        setContentView(contentView)
        resizeDialog()

        mDialogView = window!!.decorView.findViewById(android.R.id.content)
        titleTextView = contentView.findViewById(R.id.tvTitle)
        contentTextView = contentView.findViewById(R.id.tvContent)
        mPositiveBtn = contentView.findViewById(R.id.btnPositive)

        val llBtnGroup = findViewById<View>(R.id.llBtnGroup)
        val logoIv = contentView.findViewById<ImageView>(R.id.logoIv)
        logoIv.setBackgroundResource(getLogoResId(mDialogType))

        val topLayout = contentView.findViewById<LinearLayout>(R.id.topLayout)
        val triangleIv = ImageView(context)

        triangleIv.layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, Utils.dp2px(context, 10f))

        triangleIv.setImageBitmap(createTriangle(
                (Utils.getScreenSize(context).x * 0.9).toInt(),
                Utils.dp2px(context, 10f)))

        topLayout.addView(triangleIv)
        setBtnBackground(mPositiveBtn!!)
        setBottomCorners(llBtnGroup)

        val radius = Utils.dp2px(context, DEFAULT_RADIUS.toFloat())
        val outerRadii = floatArrayOf(radius.toFloat(), radius.toFloat(), radius.toFloat(), radius.toFloat(), 0f, 0f, 0f, 0f)

        val roundRectShape = RoundRectShape(outerRadii, null, null)
        val shapeDrawable = ShapeDrawable(roundRectShape)
        shapeDrawable.paint.style = Paint.Style.FILL

        shapeDrawable.paint.color = context.resources.getColor(getColorResId(mDialogType))
        val llTop = findViewById<LinearLayout>(R.id.llTop)
        llTop.background = shapeDrawable

        titleTextView!!.text = mTitle
        contentTextView!!.text = mContent
        mPositiveBtn!!.text = mBtnText
        titleTextView!!.setTextColor(context.resources.getColor(getColorResId(mDialogType)))
    }

    private fun resizeDialog() {
        val params = window!!.attributes
        params.width = (Utils.getScreenSize(context).x * 0.9).toInt()
        window!!.attributes = params
    }

    override fun onStart() {
        super.onStart()
        startWithAnimation(mIsShowAnim)
    }

    override fun dismiss() {
        dismissWithAnimation(mIsShowAnim)
    }

    private fun startWithAnimation(showInAnimation: Boolean) {
        if (showInAnimation) {
            mDialogView!!.startAnimation(mAnimIn)
        }
    }

    private fun dismissWithAnimation(showOutAnimation: Boolean) {
        if (showOutAnimation) {
            mDialogView!!.startAnimation(mAnimOut)
        } else {
            super.dismiss()
        }
    }

    private fun getLogoResId(mDialogType: Int): Int {
        when (mDialogType) {
            DIALOG_TYPE_DEFAULT ->

                return R.drawable.ic_info

            DIALOG_TYPE_HELP ->

                return R.drawable.ic_help

            DIALOG_TYPE_ERROR ->

                return R.drawable.ic_wrong

            DIALOG_TYPE_SUCCESS ->

                return R.drawable.ic_success

            DIALOG_TYPE_WARNING ->

                return R.drawable.icon_warning

            else ->

                return R.drawable.ic_info
        }
    }

    private fun getColorResId(mDialogType: Int): Int {
        when (mDialogType) {
            DIALOG_TYPE_DEFAULT ->

                return R.color.color_type_info

            DIALOG_TYPE_HELP ->

                return R.color.color_type_help

            DIALOG_TYPE_ERROR ->

                return R.color.color_type_wrong

            DIALOG_TYPE_SUCCESS ->

                return R.color.color_type_success

            DIALOG_TYPE_WARNING ->

                return R.color.color_type_warning

            else ->

                return R.color.color_type_info
        }
    }

    private fun getSelBtn(mDialogType: Int): Int {
        when (mDialogType) {
            DIALOG_TYPE_DEFAULT ->

                return R.drawable.sel_btn

            DIALOG_TYPE_HELP ->

                return R.drawable.sel_btn_help

            DIALOG_TYPE_ERROR ->

                return R.drawable.sel_btn_wrong

            DIALOG_TYPE_SUCCESS ->

                return R.drawable.sel_btn_success

            DIALOG_TYPE_WARNING ->

                return R.drawable.sel_btn_warning

            else ->

                return R.drawable.sel_btn
        }
    }

    private fun initAnimListener() {
        mAnimOut!!.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}

            override fun onAnimationEnd(animation: Animation) {
                mDialogView!!.post { callDismiss() }
            }

            override fun onAnimationRepeat(animation: Animation) {

            }
        })
    }

    private fun initListener() {
        mPositiveBtn!!.setOnClickListener {
            if (mOnPositiveListener != null) {
                mOnPositiveListener!!.onClick(this@PromptDialogBox)
            }
        }

        initAnimListener()
    }

    private fun callDismiss() {
        super.dismiss()
    }

    private fun createTriangle(width: Int, height: Int): Bitmap? {
        return if (width <= 0 || height <= 0) {
            null
        } else getBitmap(width, height, context.resources.getColor(getColorResId(mDialogType)))

    }

    private fun getBitmap(width: Int, height: Int, backgroundColor: Int): Bitmap {
        val bitmap = Bitmap.createBitmap(width, height, BITMAP_CONFIG)
        val canvas = Canvas(bitmap)

        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.color = backgroundColor
        val path = Path()
        path.moveTo(0f, 0f)
        path.lineTo(width.toFloat(), 0f)
        path.lineTo((width / 2).toFloat(), height.toFloat())
        path.close()

        canvas.drawPath(path, paint)
        return bitmap
    }

    private fun setBtnBackground(btnOk: TextView) {
        btnOk.setTextColor(createColorStateList(context.resources.getColor(getColorResId(mDialogType)),
                context.resources.getColor(R.color.color_dialog_gray)))

        btnOk.background = context.resources.getDrawable(getSelBtn(mDialogType))
    }

    private fun setBottomCorners(llBtnGroup: View) {
        val radius = Utils.dp2px(context, DEFAULT_RADIUS.toFloat())
        val outerRadii = floatArrayOf(0f, 0f, 0f, 0f, radius.toFloat(), radius.toFloat(), radius.toFloat(), radius.toFloat())

        val roundRectShape = RoundRectShape(outerRadii, null, null)
        val shapeDrawable = ShapeDrawable(roundRectShape)

        shapeDrawable.paint.color = Color.WHITE
        shapeDrawable.paint.style = Paint.Style.FILL
        llBtnGroup.background = shapeDrawable
    }

    private fun createColorStateList(normal: Int, pressed: Int, focused: Int = Color.BLACK, unable: Int = Color.BLACK): ColorStateList {
        val colors = intArrayOf(pressed, focused, normal, focused, unable, normal)
        val states = arrayOfNulls<IntArray>(6)

        states[0] = intArrayOf(android.R.attr.state_pressed, android.R.attr.state_enabled)
        states[1] = intArrayOf(android.R.attr.state_enabled, android.R.attr.state_focused)
        states[2] = intArrayOf(android.R.attr.state_enabled)
        states[3] = intArrayOf(android.R.attr.state_focused)
        states[4] = intArrayOf(android.R.attr.state_window_focused)

        states[5] = intArrayOf()
        return ColorStateList(states, colors)
    }

    fun setAnimationEnable(enable: Boolean): PromptDialogBox {
        mIsShowAnim = enable
        return this
    }

    fun setTitleText(title: CharSequence): PromptDialogBox {
        mTitle = title
        return this
    }

    fun setTitleText(resId: Int): PromptDialogBox {
        return setTitleText(context.getString(resId))
    }

    fun setContentText(content: CharSequence): PromptDialogBox {
        mContent = content
        return this
    }

    fun setContentText(resId: Int): PromptDialogBox {
        return setContentText(context.getString(resId))
    }

    fun getDialogType(): Int {
        return mDialogType
    }

    fun setDialogType(type: Int): PromptDialogBox {
        mDialogType = type
        return this
    }

    fun setPositiveListener(btnText: CharSequence, l: onPositiveListener): PromptDialogBox {
        mBtnText = btnText
        return setPositiveListener(l)
    }

    fun setPositiveListener(stringResId: Int, l: onPositiveListener): PromptDialogBox {
        return setPositiveListener(context.getString(stringResId), l)
    }

    fun setPositiveListener(l: onPositiveListener): PromptDialogBox {
        mOnPositiveListener = l
        return this
    }

    fun setAnimationIn(animIn: AnimationSet): PromptDialogBox {
        mAnimIn = animIn
        return this
    }

    fun setAnimationOut(animOut: AnimationSet): PromptDialogBox {
        mAnimOut = animOut
        initAnimListener()
        return this
    }

    interface onPositiveListener {
        fun onClick(dialog: PromptDialogBox)
    }

    companion object {
        val DIALOG_TYPE_INFO = 0
        val DIALOG_TYPE_HELP = 1
        val DIALOG_TYPE_ERROR = 2
        val DIALOG_TYPE_SUCCESS = 3
        val DIALOG_TYPE_WARNING = 4

        val DIALOG_TYPE_DEFAULT = DIALOG_TYPE_INFO
        private val BITMAP_CONFIG = Bitmap.Config.ARGB_8888

        private val DEFAULT_RADIUS = 6
    }
}
