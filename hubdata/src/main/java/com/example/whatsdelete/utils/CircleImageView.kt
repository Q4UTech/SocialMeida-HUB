package com.example.whatsdelete.utils

import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.AttributeSet
import android.widget.ImageView
import com.pds.wastatustranding.R


class CircleImageView : ImageView {

    val SCALE_TYPE = ScaleType.CENTER_CROP

    val BITMAP_CONFIG = Bitmap.Config.ARGB_8888
    val COLORDRAWABLE_DIMENSION = 2

    val DEFAULT_BORDER_WIDTH = 0
    val DEFAULT_BORDER_COLOR = Color.BLACK
    val DEFAULT_FILL_COLOR = Color.TRANSPARENT
    val DEFAULT_BORDER_OVERLAY = false

    val mDrawableRect = RectF()
    val mBorderRect = RectF()

    val mShaderMatrix = Matrix()
    val mBitmapPaint = Paint()
    val mBorderPaint = Paint()
    val mFillPaint = Paint()

    var mBorderColor = DEFAULT_BORDER_COLOR
    var mBorderWidth = DEFAULT_BORDER_WIDTH
    var mFillColor = DEFAULT_FILL_COLOR

    var mBitmap: Bitmap? = null
    var mBitmapShader: BitmapShader? = null
    var mBitmapWidth = 0
    var mBitmapHeight = 0

    var mDrawableRadius = 0f
    var mBorderRadius = 0f

    var mColorFilter: ColorFilter? = null

    var mReady = false
    var mSetupPending = false
    var mBorderOverlay = false


    constructor(context: Context) : super(context) {
        init()
    }


    constructor(context: Context, attrs: AttributeSet) : super(context, attrs, 0) {
        init()
    }


    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        val a = context.obtainStyledAttributes(
            attrs,
            R.styleable.CircleImageView, defStyle, 0
        )
        mBorderWidth = a.getDimensionPixelSize(
            R.styleable.CircleImageView_civ_border_width,
            DEFAULT_BORDER_WIDTH
        )
        mBorderColor = a.getColor(
            R.styleable.CircleImageView_civ_border_color,
            DEFAULT_BORDER_COLOR
        )
        mBorderOverlay = a.getBoolean(
            R.styleable.CircleImageView_civ_border_overlay,
            DEFAULT_BORDER_OVERLAY
        )
        mFillColor = a.getColor(
            R.styleable.CircleImageView_civ_fill_color,
            DEFAULT_FILL_COLOR
        )
        a.recycle()
        init()
    }

    open fun init() {
        super.setScaleType(SCALE_TYPE)
        mReady = true
        if (mSetupPending) {
            setup()
            mSetupPending = false
        }
    }

    override fun getScaleType(): ScaleType? {
        return SCALE_TYPE
    }

    override fun setScaleType(scaleType: ScaleType) {
        require(scaleType == SCALE_TYPE) {
            String.format(
                "ScaleType %s not supported.", scaleType
            )
        }
    }

    override fun setAdjustViewBounds(adjustViewBounds: Boolean) {
        require(!adjustViewBounds) { "adjustViewBounds not supported." }
    }

    override fun onDraw(canvas: Canvas) {
        if (mBitmap == null) {
            return
        }
        if (mFillColor != Color.TRANSPARENT) {
            canvas.drawCircle(
                getWidth() / 2.0f, getHeight() / 2.0f,
                mDrawableRadius, mFillPaint
            )
        }
        canvas.drawCircle(
            getWidth() / 2.0f, getHeight() / 2.0f,
            mDrawableRadius, mBitmapPaint
        )
        if (mBorderWidth != 0) {
            canvas.drawCircle(
                getWidth() / 2.0f, getHeight() / 2.0f,
                mBorderRadius, mBorderPaint
            )
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        setup()
    }

    fun getBorderColor(): Int {
        return mBorderColor
    }

    // @ColorInt
    fun setBorderColor(borderColor: Int) {
        if (borderColor == mBorderColor) {
            return
        }
        mBorderColor = borderColor
        mBorderPaint.color = mBorderColor
        invalidate()
    }

    // @ColorRes
    fun setBorderColorResource(borderColorRes: Int) {
        setBorderColor(getContext().getResources().getColor(borderColorRes))
    }

    fun getFillColor(): Int {
        return mFillColor
    }

    // @ColorInt
    fun setFillColor(fillColor: Int) {
        if (fillColor == mFillColor) {
            return
        }
        mFillColor = fillColor
        mFillPaint.color = fillColor
        invalidate()
    }

    // @ColorRes
    fun setFillColorResource(fillColorRes: Int) {
        setFillColor(getContext().getResources().getColor(fillColorRes))
    }

    fun getBorderWidth(): Int {
        return mBorderWidth
    }

    fun setBorderWidth(borderWidth: Int) {
        if (borderWidth == mBorderWidth) {
            return
        }
        mBorderWidth = borderWidth
        setup()
    }

    fun isBorderOverlay(): Boolean {
        return mBorderOverlay
    }

    fun setBorderOverlay(borderOverlay: Boolean) {
        if (borderOverlay == mBorderOverlay) {
            return
        }
        mBorderOverlay = borderOverlay
        setup()
    }

    override fun setImageBitmap(bm: Bitmap?) {
        super.setImageBitmap(bm)
        mBitmap = bm
        setup()
    }

    override fun setImageDrawable(drawable: Drawable?) {
        super.setImageDrawable(drawable)
        mBitmap = getBitmapFromDrawable(drawable)
        setup()
    }

    // @DrawableRes
    override fun setImageResource(resId: Int) {
        super.setImageResource(resId)
        mBitmap = getBitmapFromDrawable(getDrawable())
        setup()
    }

    override fun setImageURI(uri: Uri?) {
        super.setImageURI(uri)
        mBitmap = if (uri != null) getBitmapFromDrawable(getDrawable()) else null
        setup()
    }

    override fun setColorFilter(cf: ColorFilter) {
        if (cf === mColorFilter) {
            return
        }
        mColorFilter = cf
        mBitmapPaint.colorFilter = mColorFilter
        invalidate()
    }

    open fun getBitmapFromDrawable(drawable: Drawable?): Bitmap? {
        if (drawable == null) {
            return null
        }
        return if (drawable is BitmapDrawable) {
            (drawable as BitmapDrawable).bitmap
        } else try {
            val bitmap: Bitmap
            bitmap = if (drawable is ColorDrawable) {
                Bitmap.createBitmap(
                    COLORDRAWABLE_DIMENSION,
                    COLORDRAWABLE_DIMENSION, BITMAP_CONFIG
                )
            } else {
                Bitmap.createBitmap(
                    drawable.intrinsicWidth,
                    drawable.intrinsicHeight, BITMAP_CONFIG
                )
            }
            val canvas = Canvas(bitmap)
            drawable.setBounds(0, 0, canvas.width, canvas.height)
            drawable.draw(canvas)
            bitmap
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    open fun setup() {
        if (!mReady) {
            mSetupPending = true
            return
        }
        if (getWidth() == 0 && getHeight() == 0) {
            return
        }
        if (mBitmap == null) {
            invalidate()
            return
        }
        mBitmapShader = BitmapShader(
            mBitmap!!, Shader.TileMode.CLAMP,
            Shader.TileMode.CLAMP
        )
        mBitmapPaint.isAntiAlias = true
        mBitmapPaint.shader = mBitmapShader
        mBorderPaint.style = Paint.Style.STROKE
        mBorderPaint.isAntiAlias = true
        mBorderPaint.color = mBorderColor
        mBorderPaint.strokeWidth = mBorderWidth.toFloat()
        mFillPaint.style = Paint.Style.FILL
        mFillPaint.isAntiAlias = true
        mFillPaint.color = mFillColor
        mBitmapHeight = mBitmap!!.height
        mBitmapWidth = mBitmap!!.width
        mBorderRect[0f, 0f, getWidth().toFloat()] = getHeight().toFloat()
        mBorderRadius = Math.min(
            (mBorderRect.height() - mBorderWidth) / 2.0f,
            (mBorderRect.width() - mBorderWidth) / 2.0f
        )
        mDrawableRect.set(mBorderRect)
        if (!mBorderOverlay) {
            mDrawableRect.inset(mBorderWidth.toFloat(), mBorderWidth.toFloat())
        }
        mDrawableRadius = Math.min(
            mDrawableRect.height() / 2.0f,
            mDrawableRect.width() / 2.0f
        )
        updateShaderMatrix()
        invalidate()
    }

    open fun updateShaderMatrix() {
        val scale: Float
        var dx = 0f
        var dy = 0f
        mShaderMatrix.set(null)
        if (mBitmapWidth * mDrawableRect.height() > mDrawableRect.width()
            * mBitmapHeight
        ) {
            scale = mDrawableRect.height() / mBitmapHeight.toFloat()
            dx = (mDrawableRect.width() - mBitmapWidth * scale) * 0.5f
        } else {
            scale = mDrawableRect.width() / mBitmapWidth.toFloat()
            dy = (mDrawableRect.height() - mBitmapHeight * scale) * 0.5f
        }
        mShaderMatrix.setScale(scale, scale)
        mShaderMatrix.postTranslate(
            (dx + 0.5f).toInt() + mDrawableRect.left,
            (dy + 0.5f).toInt() + mDrawableRect.top
        )
        mBitmapShader!!.setLocalMatrix(mShaderMatrix)
    }
}