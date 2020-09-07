package com.android.stories.custom.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class User (
    val username: String,
    val id: String
): Parcelable