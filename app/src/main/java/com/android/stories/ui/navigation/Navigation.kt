package com.android.stories.ui.navigation

interface Navigation {

    fun next()

    fun previous()

    fun completed()

    fun hasPrevious(): Boolean
}