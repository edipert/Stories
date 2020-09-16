package com.android.stories.ui.custom.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.widget.LinearLayout
import com.android.stories.R
import com.android.stories.ui.custom.anim.PausableAnimation
import kotlinx.android.synthetic.main.story_progress_bar.view.*

class ProgressBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private var duration = 0L
    private var animation: PausableAnimation? = null
    private var callback: Callback? = null

    init {
        LayoutInflater.from(context).inflate(R.layout.story_progress_bar, this)
    }

    fun setCallback(callback: Callback) {
        this.callback = callback
    }

    fun setDuration(duration: Long) {
        this.duration = duration
    }

    fun setMax() {
        finish(true)
    }

    fun setMin() {
        finish(false)
    }

    fun setMinWithoutCallback() {
        maxProgress.setBackgroundResource(R.color.colorEmptyProgressBar)
        maxProgress.visibility = GONE
        frontProgress.visibility = GONE
        animation?.setAnimationListener(null)
        animation?.cancel()
    }

    fun setMaxWithoutCallback() {
        maxProgress.setBackgroundResource(R.color.colorProgressBar)
        maxProgress.visibility = VISIBLE
        frontProgress.visibility = GONE
        animation?.setAnimationListener(null)
        animation?.cancel()
    }

    private fun finish(isMax: Boolean) {
        if (isMax) maxProgress.setBackgroundResource(R.color.colorProgressBar)
        maxProgress.visibility = if (isMax) VISIBLE else GONE
        animation?.setAnimationListener(null)
        animation?.cancel()
        callback?.onFinishProgress()
    }

    fun start() {
        maxProgress.visibility = GONE

        animation = PausableAnimation(
            0f,
            1f,
            1f,
            1f,
            Animation.ABSOLUTE,
            0f,
            Animation.RELATIVE_TO_SELF,
            0f
        )

        animation?.duration = duration
        animation?.interpolator = LinearInterpolator()
        animation?.setAnimationListener(object : Animation.AnimationListener {

            override fun onAnimationStart(animation: Animation?) {
                frontProgress.visibility = VISIBLE
            }

            override fun onAnimationEnd(animation: Animation?) {
                if (callback != null) callback?.onFinishProgress()
            }

            override fun onAnimationRepeat(animation: Animation?) {

            }
        })
        frontProgress.visibility = VISIBLE
        animation?.fillAfter = true
        frontProgress.startAnimation(animation)
    }

    fun pause() {
        animation?.pause()
    }

    fun resume() {
        animation?.resume()
    }

    fun clear() {
        animation?.setAnimationListener(null)
        animation?.cancel()
        animation = null
    }

    interface Callback {
        fun onFinishProgress()
    }
}