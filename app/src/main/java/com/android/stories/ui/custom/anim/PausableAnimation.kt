package com.android.stories.ui.custom.anim

import android.view.animation.ScaleAnimation
import android.view.animation.Transformation
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicLong

class PausableAnimation(
    fromX: Float,
    toX: Float,
    fromY: Float,
    toY: Float,
    pivotXType: Int,
    pivotXValue: Float,
    pivotYType: Int,
    pivotYValue: Float
) : ScaleAnimation(fromX, toX, fromY, toY, pivotXType, pivotXValue, pivotYType, pivotYValue) {

    private var mElapsedAtPause = AtomicLong(0)
    private var mPaused = AtomicBoolean(false)

    override fun getTransformation(
        currentTime: Long,
        outTransformation: Transformation?
    ): Boolean {

        if (mPaused.get() && mElapsedAtPause.get() == 0L) {
            mElapsedAtPause.set(currentTime - startTime)
        }

        if (mPaused.get()) {
            startTime = currentTime - mElapsedAtPause.get()
        }

        return super.getTransformation(currentTime, outTransformation)
    }

    fun pause() {
        if (mPaused.get()) return

        mElapsedAtPause.set(0)
        mPaused.set(true)
    }

    fun resume() {
        mPaused.set(false)
    }
}