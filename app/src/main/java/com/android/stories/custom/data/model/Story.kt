package com.android.stories.custom.data.model

import android.os.Parcelable
import com.android.stories.custom.util.StoryType
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Story(
    val url: String,
    @StoryType
    val type: Int,
    var seen: Boolean = false
) : Parcelable