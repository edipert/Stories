package com.android.stories.di

import android.app.Activity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.android.stories.R
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.scopes.ActivityScoped

@InstallIn(ActivityComponent::class)
@Module
object MainActivityModule {

    @Provides
    @ActivityScoped
    fun provideNavController(activity: Activity): NavController {
        return Navigation.findNavController(activity, R.id.nav_host_fragment)
    }
}