package com.android.stories.ui.main

import androidx.lifecycle.MutableLiveData
import com.android.stories.ui.base.BaseViewModel

class MainViewModel : BaseViewModel() {

    val contentListSize = MutableLiveData<Int>()

    fun loadContent() {
        contentListSize.postValue(3)
    }
}