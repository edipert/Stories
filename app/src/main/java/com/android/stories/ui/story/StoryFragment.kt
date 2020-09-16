package com.android.stories.ui.story

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.CountDownTimer
import android.view.GestureDetector
import android.view.MotionEvent
import androidx.fragment.app.viewModels
import com.android.stories.R
import com.android.stories.databinding.StoryFragmentBinding
import com.android.stories.ui.base.BaseFragment
import com.android.stories.ui.custom.widget.StoryPlayerView
import com.android.stories.ui.custom.widget.StoryProgressBar
import com.android.stories.ui.navigation.Navigation
import com.android.stories.ui.navigation.Page
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.story_fragment.*
import kotlin.math.abs


@AndroidEntryPoint
class StoryFragment : BaseFragment<StoryViewModel, StoryFragmentBinding>(), Page,
    StoryPlayerView.StoryStateListener, StoryProgressBar.StoryProgressBarListener {

    companion object {
        fun newInstance(index: Int) = StoryFragment().apply {
            arguments = Bundle().apply {
                putInt("index", index)
            }
        }
    }

    override val viewModel: StoryViewModel by viewModels()

    private var navigation: Navigation? = null

    override fun getLayoutId(): Int = R.layout.story_fragment

    private val timer = object : CountDownTimer(250, 50) {
        var millisUntilFinished = 0L

        override fun onTick(millisUntilFinished: Long) {
            this.millisUntilFinished = millisUntilFinished
        }

        override fun onFinish() {
            storyPlayerView.pause()
            storyProgressBar.pause()
            millisUntilFinished = 0
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initView()
        setOnTouchListeners()
        observe()

        arguments?.let {
            viewModel.loadContent(it.getInt("index"))
        }
    }

    private fun initView() {
        storyPlayerView.setStoryStateListener(this)
        storyProgressBar.setStoryProgressBarListener(this)
    }

    fun setNavigation(navigation: Navigation) {
        this.navigation = navigation
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setOnTouchListeners() {
        val gesture =
            GestureDetector(requireActivity(), object : GestureDetector.SimpleOnGestureListener() {
                val SWIPE_MIN_DISTANCE = 120
                val SWIPE_THRESHOLD_VELOCITY = 200

                override fun onFling(
                    e1: MotionEvent?,
                    e2: MotionEvent?,
                    velocityX: Float,
                    velocityY: Float
                ): Boolean {
                    if (e1 == null || e2 == null) return super.onFling(e1, e2, velocityX, velocityY)

                    // If swipe top to bottom
                    return e2.y - e1.y > SWIPE_MIN_DISTANCE && abs(velocityX) > SWIPE_THRESHOLD_VELOCITY
                    //((e2.x - e1.x > SWIPE_MIN_DISTANCE && abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) && navigation?.hasPrevious() == false)
                }
            })

        viewPrevious.setOnTouchListener { _, event ->
            if (gesture.onTouchEvent(event)) {
                navigation?.completed()
                return@setOnTouchListener false
            }

            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    timer.start()
                }

                MotionEvent.ACTION_UP -> {
                    if (timer.millisUntilFinished > 0) {
                        timer.cancel()
                        if (navigation?.hasPrevious() == true || viewModel.hasPreviousStory()) {
                            storyProgressBar.previous()
                        }
                    } else {
                        storyPlayerView.resume()
                        storyProgressBar.resume()
                    }
                }
            }

            true
        }

        viewNext.setOnTouchListener { _, event ->
            if (gesture.onTouchEvent(event)) {
                navigation?.completed()
                return@setOnTouchListener false
            }

            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    timer.start()
                }
                MotionEvent.ACTION_UP -> {
                    if (timer.millisUntilFinished > 0) {
                        timer.cancel()
                        storyProgressBar.next()
                    } else {
                        storyPlayerView.resume()
                        storyProgressBar.resume()
                    }
                }
            }

            true
        }
    }

    private fun observe() {
        viewModel.content.observe(viewLifecycleOwner) {
            storyProgressBar.setCount(it.storyList.size)
        }

        viewModel.currentStory.observe(viewLifecycleOwner) {
            storyPlayerView.setStory(it.url, it.type)
        }

        viewModel.currentIndex.observe(viewLifecycleOwner) {
            storyProgressBar.startFrom(it)
        }

        viewModel.navigation.observe(viewLifecycleOwner) {
            if (it) navigation?.next() else navigation?.previous()
        }
    }

    override fun onReady(duration: Long) {
        storyProgressBar.setDuration(duration)
        storyProgressBar.start()
    }

    override fun onNext() {
        viewModel.next()
    }

    override fun onPrev() {
        viewModel.previous()
    }

    override fun onComplete() {
        viewModel.next()
    }

    override fun onPageChanging() {
        pause()
    }

    override fun onPageReleased() {
        timer.cancel()
        resume()
    }

    override fun onPageClosed() {
        stop()
    }

    private fun stop() {
        storyPlayerView.stop()
        storyProgressBar.stop()
    }

    private fun pause() {
        if (storyPlayerView.isPlaying()) {
            storyPlayerView.pause()
            storyProgressBar.pause()
        }
    }

    private fun resume() {
        if (storyPlayerView.isPaused()) {
            timer.cancel()
            storyPlayerView.resume()
            storyProgressBar.resume()
        }
    }

    override fun onPause() {
        super.onPause()
        stop()
    }
}