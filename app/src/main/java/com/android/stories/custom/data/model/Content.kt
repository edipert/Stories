package com.android.stories.custom.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Content(
    val username: String,
    val imageUrl: String,
    val storyList: List<Story>,
    var seenAll: Boolean = false,
    var lastSeenStoryIndex: Int = -1
) : Parcelable