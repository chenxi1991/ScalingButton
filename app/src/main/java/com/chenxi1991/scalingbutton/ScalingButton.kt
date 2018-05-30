package com.chenxi1991.scalingbutton

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.graphics.drawable.ScaleDrawable
import android.os.Bundle
import android.os.Parcelable
import android.support.v4.view.animation.FastOutSlowInInterpolator
import android.util.AttributeSet
import android.widget.ImageButton

class ScalingButton @JvmOverloads constructor(context: Context, attributeSet: AttributeSet? = null, defStyleAttr: Int = 0) : ImageButton(context, attributeSet, defStyleAttr) {

    private val interpolator: FastOutSlowInInterpolator
    private val SCALE_TOTAL = 1f
    private val ALPHA_TOTAL = 255f

    private var unselectedImageId: Int
    private var selectedImageId: Int
    private var animationDuration: Int
    private var mState: Boolean = false

    private val unselectedDrawable: Drawable
    private val selectedDrawable: Drawable

    init {
        val array = context.obtainStyledAttributes(attributeSet, R.styleable.ScalingButton, 0, 0)
        this.unselectedImageId = array.getResourceId(R.styleable.ScalingButton_unselected_image, -1)
        this.selectedImageId = array.getResourceId(R.styleable.ScalingButton_selected_image, -1)
        this.animationDuration = array.getInteger(R.styleable.ScalingButton_duration, -1)
        this.mState = array.getBoolean(R.styleable.ScalingButton_state, false)
        array.recycle()
        if (unselectedImageId == -1 || selectedImageId == -1 || animationDuration == -1)
            throw IllegalArgumentException()

        this.unselectedDrawable = makeScaledDrawable(unselectedImageId)
        this.selectedDrawable = resources.getDrawable(selectedImageId, null)
        this.interpolator = FastOutSlowInInterpolator()

        if (mState) {
            setImageDrawable(selectedDrawable)
        } else {
            setImageDrawable(unselectedDrawable)
        }

        this.setOnClickListener({
            toggleState()
        })
    }

    override fun onSaveInstanceState(): Parcelable {
        val bundle = Bundle()
        bundle.putParcelable("superState", super.onSaveInstanceState())
        bundle.putInt("duration", animationDuration)
        bundle.putInt("unselectedImageId", unselectedImageId)
        bundle.putInt("selectedImageId", selectedImageId)
        bundle.putBoolean("state", mState)
        return bundle
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        if (state is Bundle) {
            this.animationDuration = state.getInt("duration")
            this.unselectedImageId = state.getInt("unselectedImageId")
            this.selectedImageId = state.getInt("selectedImageId")
            this.mState = state.getBoolean("state")
            super.onRestoreInstanceState(state.getParcelable("superState"))
        }
    }

    private fun animate(from: Drawable, to: Drawable) {
        val animator = ValueAnimator.ofFloat(0f, SCALE_TOTAL)
        animator.duration = animationDuration.toLong()
        animator.interpolator = interpolator
        animator.addUpdateListener({
            val value = it.animatedValue as Float
            val left = SCALE_TOTAL - value
            val fd = makeDrawable(from, left, value * ALPHA_TOTAL)
            val td = makeDrawable(to, value, left * ALPHA_TOTAL)
            setImageDrawable(LayerDrawable(arrayOf(td, fd)))
        })
        animator.start()
    }

    private fun makeScaledDrawable(id: Int): Drawable {
        val bm = BitmapFactory.decodeResource(resources, id)
        val matrix = Matrix()
        matrix.postScale(0.8f, 0.8f)
        val newBm = Bitmap.createBitmap(bm, 0, 0, bm.width, bm.height, matrix, true)

        val bgBm = Bitmap.createBitmap(bm.width, bm.height, Bitmap.Config.ARGB_8888)
        bgBm.setHasAlpha(true)
        val canvas = Canvas(bgBm)
        canvas.drawBitmap(newBm, bm.width * 0.1f, bm.height * 0.1f, null)

        return BitmapDrawable(resources, bgBm)
    }

    private fun makeDrawable(drawable: Drawable, scale: Float, alpha: Float): Drawable {
        val scaleDrawable = ScaleDrawable(drawable, 0, scale, scale)
        scaleDrawable.level = 1
        scaleDrawable.alpha = alpha.toInt()
        return scaleDrawable
    }

    public fun getState(): Boolean {
        return mState
    }

    public fun toggleState() {
        if (mState) {
            mState = false
            animate(unselectedDrawable, selectedDrawable)
        } else {
            mState = true
            animate(selectedDrawable, unselectedDrawable)
        }
    }

}