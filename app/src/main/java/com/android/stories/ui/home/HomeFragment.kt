package com.android.stories.ui.home

import android.os.Bundle
import androidx.fragment.app.viewModels
import com.android.stories.R
import com.android.stories.databinding.HomeFragmentBinding
import com.android.stories.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<HomeViewModel, HomeFragmentBinding>() {

    override val viewModel: HomeViewModel by viewModels()

    override fun getLayoutId() = R.layout.home_fragment

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.loadContents()
    }
}