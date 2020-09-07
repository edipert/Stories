package com.android.stories.ui.story

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.OnLifecycleEvent
import com.android.stories.custom.data.Content
import com.android.stories.custom.data.Story
import com.android.stories.custom.data.StoryData
import com.android.stories.ui.base.BaseViewModel

class StoryViewModel @ViewModelInject constructor(

) : BaseViewModel() {

    val content = MutableLiveData<Content>()
    val currentStory = MutableLiveData<Story>()
    val navigation = MutableLiveData<Boolean>()
    val currentIndex = MutableLiveData<Int>()

    fun loadContent() {
        content.value = StoryData.getRandomContent()
    }

    fun next() {
        content.value?.let {
            if (it.lastSeenStoryIndex + 1 < it.storyList.size) {
                it.lastSeenStoryIndex++
                currentStory.value = it.storyList[it.lastSeenStoryIndex]
            } else {
                navigation.value = true
            }
        }
    }

    fun previous() {
        content.value?.let {
            if (it.lastSeenStoryIndex - 1 >= 0) {
                it.lastSeenStoryIndex--
                currentStory.value = it.storyList[it.lastSeenStoryIndex]
            } else {
                navigation.value = false
            }
        }
    }

    fun hasPreviousStory() = content.value?.lastSeenStoryIndex ?: 0 > 0

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
        content.value?.let {
            if (it.lastSeenStoryIndex >= 0)
                currentIndex.value = it.lastSeenStoryIndex + 1
            else
                currentIndex.value = 0
        } ?: run {
            currentIndex.value = 0
        }

        next()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPause() {
        content.value?.let {
            if (it.lastSeenStoryIndex >= 0)
                it.lastSeenStoryIndex--
        }
    }
}