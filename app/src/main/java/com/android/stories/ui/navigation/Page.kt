package com.android.stories.ui.navigation

interface Page {
    fun onPageChanging()

    fun onPageReleased()
}