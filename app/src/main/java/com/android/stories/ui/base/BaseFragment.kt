package com.android.stories.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import com.android.stories.BR

abstract class BaseFragment<T : BaseViewModel, B : ViewDataBinding> : Fragment(), LifecycleOwner {

    protected abstract val viewModel: T

    private lateinit var binding: B

    protected abstract fun getLayoutId(): Int

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        this.binding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.viewLifecycleOwner.lifecycle.addObserver(viewModel)
        this.binding.lifecycleOwner = viewLifecycleOwner
        this.binding.setVariable(BR.viewModel, viewModel)
        this.binding.executePendingBindings()
    }
}