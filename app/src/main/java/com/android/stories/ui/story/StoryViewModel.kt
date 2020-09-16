package com.android.stories.ui.story

import androidx.databinding.ObservableField
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.OnLifecycleEvent
import androidx.navigation.NavController
import com.android.stories.custom.data.StoryData
import com.android.stories.custom.data.model.Content
import com.android.stories.custom.data.model.Story
import com.android.stories.ui.base.BaseViewModel

class StoryViewModel @ViewModelInject constructor(

) : BaseViewModel() {

    val content = MutableLiveData<Content>()
    val currentStory = MutableLiveData<Story>()
    val navigation = MutableLiveData<Boolean>()
    val currentIndex = MutableLiveData<Int>()

    val username = ObservableField<String>()
    val userImageUrl = ObservableField<String>()

    //Load user stories
    fun loadContent(index: Int) {
        StoryData.contents.getOrNull(index)?.let {
            if (it.lastSeenStoryIndex + 1 == it.storyList.size)
                it.lastSeenStoryIndex = -1

            username.set(it.username)
            userImageUrl.set(it.imageUrl)

            content.value = it
        }
    }

    // Load next story
    fun next() {
        content.value?.let {
            // Check last watched story, if it's all watched navigate the next user stories
            if (it.lastSeenStoryIndex + 1 < it.storyList.size) {
                it.lastSeenStoryIndex++
                currentStory.value = it.storyList[it.lastSeenStoryIndex]
            } else {
                navigation.value = true
            }
        }
    }

    // Load previous story
    fun previous() {
        content.value?.let {
            // Check last watched story, if it's first story navigate the previous user stories
            if (it.lastSeenStoryIndex - 1 >= 0) {
                it.lastSeenStoryIndex--
                currentStory.value = it.storyList[it.lastSeenStoryIndex]
            } else {
                navigation.value = false
            }
        }
    }

    fun hasPreviousStory() = content.value?.lastSeenStoryIndex ?: 0 > 0

    // Set last watched story index on resume
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

    // Decrease last watched story index
    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPause() {
        content.value?.let {
            if (it.lastSeenStoryIndex >= 0)
                it.lastSeenStoryIndex--
        }
    }
}