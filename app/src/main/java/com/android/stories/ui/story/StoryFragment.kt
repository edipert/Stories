package com.android.stories.ui.story

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.CountDownTimer
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


@AndroidEntryPoint
class StoryFragment : BaseFragment<StoryViewModel, StoryFragmentBinding>(), Page,
    StoryPlayerView.StoryStateListener, StoryProgressBar.StoryProgressBarListener {

    companion object {

        private const val TAG_INDEX = "TAG_INDEX"

        fun newInstance() = StoryFragment()
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
        setOnClickListeners()
        observe()

        viewModel.loadContent()
    }

    private fun initView() {
        storyPlayerView.setStoryStateListener(this)
        storyProgressBar.setStoryProgressBarListener(this)
    }

    fun setNavigation(navigation: Navigation) {
        this.navigation = navigation
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setOnClickListeners() {
        viewPrevious.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    timer.start()
                }
                MotionEvent.ACTION_UP -> {
                    if (timer.millisUntilFinished > 0) {
                        timer.cancel()
                        storyProgressBar.previous()
                    } else {
                        storyPlayerView.resume()
                        storyProgressBar.resume()
                    }
                }
            }

            true
        }

        viewNext.setOnTouchListener { _, event ->
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
        navigation?.next()
    }

    override fun onPageChanging() {
        pause()
    }

    override fun onPageReleased() {
        resume()
    }

    override fun onPause() {
        super.onPause()
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
            storyPlayerView.resume()
            storyProgressBar.resume()
        }
    }
}