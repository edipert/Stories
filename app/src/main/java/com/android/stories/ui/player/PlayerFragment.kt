package com.android.stories.ui.player

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.widget.ViewPager2
import com.android.stories.R
import com.android.stories.custom.data.StoryData
import com.android.stories.databinding.PlayerFragmentBinding
import com.android.stories.ui.adapter.StoryPageAdapter
import com.android.stories.ui.base.BaseFragment
import com.android.stories.ui.navigation.Navigation
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.player_fragment.*
import javax.inject.Inject

@AndroidEntryPoint
class PlayerFragment : BaseFragment<PlayerViewModel, PlayerFragmentBinding>(), Navigation {

    private lateinit var adapter: StoryPageAdapter

    override val viewModel: PlayerViewModel by viewModels()

    private val args: PlayerFragmentArgs by navArgs()

    @Inject
    lateinit var navController: NavController

    override fun getLayoutId() = R.layout.player_fragment

    private var contentListSize = StoryData.contents.size

    private val pageTransformer = ViewPager2.PageTransformer { page, position ->
        page.cameraDistance = (page.width * 20).toFloat()
        page.pivotX = if (position < 0f) page.width.toFloat() else 0f
        page.pivotY = page.height * 0.5f
        page.rotationY = 90f * position
    }

    private val stateChangeCallback = object : ViewPager2.OnPageChangeCallback() {

        var isLastPageSwiped = false
        var counter = 0

        override fun onPageScrolled(
            position: Int,
            positionOffset: Float,
            positionOffsetPixels: Int
        ) {
            if (position == contentListSize - 1 && positionOffset == 0f && !isLastPageSwiped) {
                if (counter != 0) {
                    isLastPageSwiped = true
                    completed()
                }
                counter++
            } else {
                counter = 0
            }
        }

        override fun onPageScrollStateChanged(state: Int) {
            if (viewPager.scrollState == ViewPager2.SCROLL_STATE_DRAGGING) {
                adapter.getPage(viewPager.currentItem).onPageChanging()
            } else if (viewPager.scrollState == ViewPager2.SCROLL_STATE_IDLE) {
                adapter.getPage(viewPager.currentItem).onPageReleased()
            }
        }
    }

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            completed()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(onBackPressedCallback)

        adapter = StoryPageAdapter(requireActivity(), contentListSize, this)

        viewPager.adapter = adapter
        viewPager.setPageTransformer(pageTransformer)
        viewPager.registerOnPageChangeCallback(stateChangeCallback)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setCurrentPage(args.index)
    }

    override fun next() {
        if (viewPager.currentItem + 1 < contentListSize) {
            setCurrentPage(viewPager.currentItem + 1)
        } else {
            completed()
        }
    }

    override fun previous() {
        if (viewPager.currentItem > 0) {
            setCurrentPage(viewPager.currentItem - 1)
        }
    }

    override fun completed() {
        onBackPressedCallback.remove()
        viewPager.unregisterOnPageChangeCallback(stateChangeCallback)
        adapter.getPage(viewPager.currentItem).onPageClosed()
        navController.popBackStack()
    }

    override fun hasPrevious(): Boolean {
        return viewPager.currentItem > 0
    }

    private fun setCurrentPage(currentPage: Int) {
        viewPager.post {
            viewPager.setCurrentItem(currentPage, true)
        }
    }
}