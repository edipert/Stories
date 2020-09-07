package com.android.stories.ui.custom.util

import android.os.CountDownTimer

class Timer(
    private val millisInFuture: Long
) {
    private var millisUntilFinished = millisInFuture

    private var timer: CountDownTimer? = null

    private var isRunning = false

    private var onFinish: () -> Unit = {
        millisUntilFinished = 0L
    }

    fun start() {
        timer = createTimer(millisInFuture)
        isRunning = true
        timer?.start()
    }

    fun pause() {
        if (isRunning) {
            isRunning = false
            timer?.cancel()
        }
    }

    fun resume() {
        if (!isRunning) {
            timer = createTimer(millisUntilFinished)
            isRunning = true
            timer?.start()
        }
    }

    fun cancel() {
        isRunning = false
        millisUntilFinished = 0L
        timer?.cancel()
    }

    fun onFinish(onFinish: () -> Unit) {
        this.onFinish = onFinish
    }

    private fun createTimer(millisInFuture: Long) = object : CountDownTimer(millisInFuture, 10) {
        override fun onTick(millisUntilFinished: Long) {
            this@Timer.millisUntilFinished = millisUntilFinished
        }

        override fun onFinish() {
            this@Timer.millisUntilFinished = 0
            this@Timer.isRunning = false
            this@Timer.onFinish()
        }
    }
}