package com.android.stories.custom.data

import kotlin.random.Random

object StoryData {

    private val videoList = listOf(
        "http://techslides.com/demos/sample-videos/small.mp4",
        "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerJoyrides.mp4",
        "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerEscapes.mp4"
    )

    private val imageList = listOf(
        "https://www.gstatic.com/webp/gallery/1.jpg",
        "https://www.gstatic.com/webp/gallery/2.jpg",
        "https://www.gstatic.com/webp/gallery/4.jpg"
    )

    fun getRandomContent(): Content {
        val storyList = arrayListOf<Story>()

        for (i in 0 until Random.nextInt(1, 10)) {

            when (Random.nextInt(0, 2)) {
                0 -> storyList.add(Story(videoList[Random.nextInt(0, videoList.size)], 0))
                1 -> storyList.add(Story(imageList[Random.nextInt(0, imageList.size)], 1))
            }
        }

        return Content(
            username = "edip",
            imageUrl = "",
            storyList = storyList
        )
    }
}