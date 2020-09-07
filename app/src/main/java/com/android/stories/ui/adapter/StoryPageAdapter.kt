package com.android.stories.ui.adapter

import android.util.SparseArray
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.android.stories.ui.navigation.Navigation
import com.android.stories.ui.navigation.Page
import com.android.stories.ui.story.StoryFragment

class StoryPageAdapter(
    activity: FragmentActivity,
    private val count: Int,
    private val navigation: Navigation
) : FragmentStateAdapter(activity) {

    private val pageArray = SparseArray<Page>()

    override fun getItemCount(): Int {
        return count
    }

    override fun createFragment(position: Int): Fragment {
        val fragment = StoryFragment.newInstance()
        fragment.setNavigation(navigation)
        pageArray.put(position, fragment)
        return fragment
    }

    fun getPage(position: Int): Page = pageArray.get(position)
}