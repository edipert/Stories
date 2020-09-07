package com.android.stories.ui.main

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.android.stories.R
import com.android.stories.databinding.MainFragmentBinding
import com.android.stories.ui.adapter.StoryPageAdapter
import com.android.stories.ui.base.BaseFragment
import com.android.stories.ui.navigation.Navigation
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.main_fragment.*

@AndroidEntryPoint
class MainFragment : BaseFragment<MainViewModel, MainFragmentBinding>(), Navigation {

    private lateinit var adapter: StoryPageAdapter

    override val viewModel: MainViewModel by viewModels()

    override fun getLayoutId() = R.layout.main_fragment

    private val pageTransformer = ViewPager2.PageTransformer { page, position ->
        page.cameraDistance = (page.width * 20).toFloat()
        page.pivotX = if (position < 0f) page.width.toFloat() else 0f
        page.pivotY = page.height * 0.5f
        page.rotationY = 90f * position

        adapter.getPage(viewPager.currentItem).let {
            if (position == 0f) {
                it.onPageReleased()
            } else {
                it.onPageChanging()
            }
        }
    }

    private var contentListSize = 0

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        observe()

        viewModel.loadContent()
    }

    private fun observe() {
        viewModel.contentListSize.observe(viewLifecycleOwner) {
            this.contentListSize = it
            adapter = StoryPageAdapter(requireActivity(), it, this)

            viewPager.adapter = adapter
            viewPager.setPageTransformer(pageTransformer)
        }
    }

    override fun next() {
        if (viewPager.currentItem < contentListSize) {
            viewPager.setCurrentItem(viewPager.currentItem + 1, true)
        }
    }

    override fun previous() {
        if (viewPager.currentItem > 0) {
            viewPager.setCurrentItem(viewPager.currentItem - 1, true)
        }
    }

    override fun hasPrevious(): Boolean {
        return viewPager.currentItem > 0
    }
}