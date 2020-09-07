package com.android.stories.custom.util

import androidx.annotation.IntDef

@IntDef(
    StoryType.NONE,
    StoryType.VIDEO,
    StoryType.IMAGE
)
annotation class StoryType {
    companion object {
        const val NONE = -1
        const val VIDEO = 0
        const val IMAGE = 1
    }
}