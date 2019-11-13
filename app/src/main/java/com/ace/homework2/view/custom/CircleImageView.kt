package com.ace.homework2.view.custom

import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.annotation.ColorInt
import androidx.appcompat.widget.AppCompatImageView
import com.ace.homework2.R

class CircleImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {

    companion object {
        private val SCALE_TYPE = ScaleType.CENTER_CROP
        private const val DEFAULT_CIRCLE_BACKGROUND_COLOR = Color.BLACK
        private const val DEFAULT_TEXT_COLOR = Color.YELLOW
        private const val DEFAULT_TEXT_LETTER = "No name"
    }

    private val mDrawableRect = RectF()
    private val mBorderRect = RectF()

    private val mShaderMatrix = Matrix()
    private val mBitmapPaint = Paint()
    private val paintCircle = Paint()
    private val paintLetter = Paint()

    private var mBitmap: Bitmap? = null
    private var mBitmapShader: BitmapShader? = null
    private var mBitmapWidth: Int = 0
    private var mBitmapHeight: Int = 0

    private var mDrawableRadius: Float = 0.toFloat()
    private var text: String = ""
    @ColorInt
    private var textColor: Int = 0
    @ColorInt
    private var bgColor: Int = 0

    init {
        if (attrs != null) {
            val array =
                context.obtainStyledAttributes(
                    attrs, R.styleable.CircleImageView, defStyleAttr, 0
                )
            setBackgroundCircleColor(
                array.getColor(
                    R.styleable.CircleImageView_backgColor,
                    DEFAULT_CIRCLE_BACKGROUND_COLOR
                )
            )

            setTextColor(array.getColor(R.styleable.CircleImageView_textColor, DEFAULT_TEXT_COLOR))

            setText(array.getString(R.styleable.CircleImageView_txt) ?: DEFAULT_TEXT_LETTER)
            array.recycle()
            this.scaleType = SCALE_TYPE
            setup()
        }
    }

    private fun setText(text: String) {
        this.text = text.toUpperCase()
    }

    private fun setBackgroundCircleColor(@ColorInt color: Int) {
        bgColor = color
    }

    private fun setTextColor(@ColorInt color: Int) {
        textColor = color
    }


    override fun onDraw(canvas: Canvas) {

        if (mBitmap == null) {

            paintCircle.color = bgColor
            paintCircle.flags = Paint.ANTI_ALIAS_FLAG
            paintCircle.style = Paint.Style.FILL

            paintLetter.textSize = 90f
            paintLetter.color = textColor
            paintLetter.flags = Paint.ANTI_ALIAS_FLAG
            paintLetter.textAlign = Paint.Align.CENTER

            val height = this.height
            val width = this.width

            val diameter = if (height > width) height else width
            val radius = diameter / 2f

            canvas.drawCircle(diameter / 2f, diameter / 2f, radius, paintCircle)
            canvas.drawText(text, 0, 1, diameter / 2f, diameter / 2f + paintLetter.textSize / 4, paintLetter)

        } else canvas.drawCircle(mDrawableRect.centerX(), mDrawableRect.centerY(), mDrawableRadius, mBitmapPaint)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        setup()
    }

    override fun setImageDrawable(drawable: Drawable?) {
        super.setImageDrawable(drawable)
        initializeBitmap()
    }

    private fun getBitmapFromDrawable(drawable: Drawable?): Bitmap? {
        return if (drawable is BitmapDrawable) drawable.bitmap else null
    }

    private fun initializeBitmap() {
        mBitmap = getBitmapFromDrawable(drawable)
        setup()
    }

    private fun setup() {
        if (width == 0 && height == 0) return

        if (mBitmap == null) {
            invalidate()
            return
        }

        mBitmapShader = BitmapShader(mBitmap!!, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)

        mBitmapPaint.isAntiAlias = true
        mBitmapPaint.shader = mBitmapShader

        mBitmapHeight = mBitmap!!.height
        mBitmapWidth = mBitmap!!.width

        mBorderRect.set(calculateBounds())

        mDrawableRect.set(mBorderRect)
        mDrawableRadius = (mDrawableRect.height() / 2.0f).coerceAtMost(mDrawableRect.width() / 2.0f)

        updateShaderMatrix()
        invalidate()
    }

    private fun updateShaderMatrix() {
        val scale: Float
        var dx = 0f
        var dy = 0f

        mShaderMatrix.set(null)

        if (mBitmapWidth * mDrawableRect.height() > mDrawableRect.width() * mBitmapHeight) {
            scale = mDrawableRect.height() / mBitmapHeight
            dx = (mDrawableRect.width() - mBitmapWidth * scale) * 0.5f
        } else {
            scale = mDrawableRect.width() / mBitmapWidth
            dy = (mDrawableRect.height() - mBitmapHeight * scale) * 0.5f
        }

        mShaderMatrix.setScale(scale, scale)
        mShaderMatrix.postTranslate((dx + 0.5f) + mDrawableRect.left, (dy + 0.5f) + mDrawableRect.top)

        mBitmapShader?.setLocalMatrix(mShaderMatrix)

    }

    private fun calculateBounds(): RectF {
        val availableWidth = width - paddingLeft - paddingRight
        val availableHeight = height - paddingTop - paddingBottom

        val sideLength = availableWidth.coerceAtMost(availableHeight)

        val left = paddingLeft + (availableWidth - sideLength) / 2f
        val top = paddingTop + (availableHeight - sideLength) / 2f

        return RectF(left, top, left + sideLength, top + sideLength)
    }
}