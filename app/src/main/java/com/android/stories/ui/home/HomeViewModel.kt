package com.android.stories.ui.home

import androidx.hilt.lifecycle.ViewModelInject
import androidx.navigation.NavController
import com.android.stories.custom.data.StoryData
import com.android.stories.ui.adapter.ContentAdapter
import com.android.stories.ui.base.BaseViewModel

class HomeViewModel @ViewModelInject constructor(
    private val navController: NavController?
) : BaseViewModel() {

    val contentAdapter = ContentAdapter {
        navController?.navigate(
            HomeFragmentDirections.actionHomeFragmentToPlayerFragment(it)
        )
    }

    fun loadContents() {
        contentAdapter.setItems(
            StoryData.contents
        )
    }
}