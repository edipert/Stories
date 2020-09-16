package com.android.stories.ui.home

import android.os.Bundle
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.fragment.app.viewModels
import com.android.stories.R
import com.android.stories.databinding.HomeFragmentBinding
import com.android.stories.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.abs

@AndroidEntryPoint
class HomeFragment : BaseFragment<HomeViewModel, HomeFragmentBinding>() {

    override val viewModel: HomeViewModel by viewModels()

    override fun getLayoutId() = R.layout.home_fragment

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val gesture = GestureDetector(requireActivity(), object : GestureDetector.SimpleOnGestureListener() {

                override fun onDown(e: MotionEvent?): Boolean {
                    return true
                }

                override fun onFling(
                    e1: MotionEvent?,
                    e2: MotionEvent?,
                    velocityX: Float,
                    velocityY: Float
                ): Boolean {
                    if (e1 == null || e2 == null) return super.onFling(e1, e2, velocityX, velocityY)

                    val SWIPE_MIN_DISTANCE = 120
                    val SWIPE_MAX_OFF_PATH = 250
                    val SWIPE_THRESHOLD_VELOCITY = 200

                    //if (abs(e1.y - e2.y) > SWIPE_MAX_OFF_PATH)
                        //return false

                    if (e1.x - e2.x > SWIPE_MIN_DISTANCE && abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                        Log.i("SWIPEFRAG", "Right to Left")
                    } else if (e2.x - e1.x > SWIPE_MIN_DISTANCE && abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                        Log.i("SWIPEFRAG", "Left to Right")
                    } else if (e2.y - e1.y > SWIPE_MIN_DISTANCE && abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                        Log.i("SWIPEFRAG", "Top to Bottom")
                    }

                    return super.onFling(e1, e2, velocityX, velocityY)
                }
            })

        view.setOnTouchListener { _, event ->
            return@setOnTouchListener gesture.onTouchEvent(event)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.loadContents()
    }
}