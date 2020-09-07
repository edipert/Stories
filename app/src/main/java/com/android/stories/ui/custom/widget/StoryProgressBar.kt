package com.android.stories.ui.custom.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import com.android.stories.R

class StoryProgressBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val PROGRESS_BAR_LAYOUT_PARAM = LayoutParams(0, LayoutParams.WRAP_CONTENT, 1f)
    private val SPACE_LAYOUT_PARAM = LayoutParams(5, LayoutParams.WRAP_CONTENT)

    private var storyCount = -1

    private var currentIndex = 0

    private var progressBarListener: StoryProgressBarListener? = null

    private val progressBarList = arrayListOf<ProgressBar>()

    var isComplete = false
    private var isSkipStart = false
    private var isReverseStart = false

    init {
        readAttributes(context, attrs)
    }

    private fun readAttributes(context: Context, attributeSet: AttributeSet?) {
        orientation = HORIZONTAL

        val typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.StoryProgressBar)
        storyCount = typedArray.getInt(R.styleable.StoryProgressBar_storyCount, 0)
        typedArray.recycle()
        initialize()
    }

    private fun initialize() {
        removeAllViews()
        progressBarList.clear()

        for (i in 0 until storyCount) {
            val progressBar = ProgressBar(context)
            progressBar.layoutParams = PROGRESS_BAR_LAYOUT_PARAM
            progressBarList.add(progressBar)
            addView(progressBar)
            progressBarList[i].setCallback(callback())

            if ((i + 1) < storyCount) {
                val spaceView = View(context)
                spaceView.layoutParams = SPACE_LAYOUT_PARAM
                addView(spaceView)
            }
        }
    }

    fun setStoryProgressBarListener(listener: StoryProgressBarListener) {
        this.progressBarListener = listener
    }

    fun next() {
        if (isSkipStart || isReverseStart) return
        if (currentIndex < 0) currentIndex = 0

        val progressBar = progressBarList[currentIndex]
        isSkipStart = true
        progressBar.setMax()
    }

    fun previous() {
        if (isSkipStart || isReverseStart) return
        if (currentIndex < 0) {
            progressBarListener?.onPrev()
            return
        }

        val progressBar = progressBarList[currentIndex]
        isReverseStart = true
        progressBar.setMin()
    }

    fun setDuration(duration: Long) {
        if (progressBarList.isNotEmpty() && currentIndex > -1) {
            progressBarList[currentIndex].setDuration(duration)
        }
    }

    private fun callback() = object : ProgressBar.Callback {
        override fun onFinishProgress() {
            if (isReverseStart) {
                if (currentIndex >= 0)
                    currentIndex--
                progressBarListener?.onPrev()
                isReverseStart = false
                return
            }

            val next = currentIndex + 1
            if (next <= (progressBarList.size - 1)) {
                currentIndex = next
                progressBarListener?.onNext()
            } else {
                isComplete = true
                progressBarListener?.onComplete()
            }

            isSkipStart = false
        }
    }

    fun start() {
        if (currentIndex < 0) return

        progressBarList[currentIndex].start()
    }

    fun startFrom(index: Int) {
        for (i in 0 until index) {
            progressBarList[i].setMaxWithoutCallback()
        }

        for (i in index until storyCount) {
            progressBarList[i].setMinWithoutCallback()
        }

        this.isComplete = false
        this.currentIndex = index
    }

    fun setCount(count: Int) {
        this.storyCount = count
        initialize()
    }

    fun pause() {
        if (currentIndex < 0) return
        progressBarList[currentIndex].pause()
    }

    fun resume() {
        if (currentIndex < 0) return
        progressBarList[currentIndex].resume()
    }

    fun stop() {
        progressBarList.forEach {
            it.clear()
        }
    }

    interface StoryProgressBarListener {
        fun onNext()
        fun onPrev()
        fun onComplete()
    }
}