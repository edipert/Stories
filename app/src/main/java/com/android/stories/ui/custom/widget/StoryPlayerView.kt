package com.android.stories.ui.custom.widget

import android.content.Context
import android.net.Uri
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.LayoutRes
import androidx.constraintlayout.widget.ConstraintLayout
import com.android.stories.R
import com.android.stories.custom.util.StoryType
import com.bumptech.glide.Glide
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import kotlinx.android.synthetic.main.view_story_player.view.*


class StoryPlayerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    companion object {
        @LayoutRes
        private const val LAYOUT_ID = R.layout.view_story_player

        private const val DEFAULT_IMAGE_STORY_DURATION = 5000L


        const val KEY_USER_AGENT = "exoplayer-codelab"
    }

    private enum class State {
        PLAYING, PAUSED, STOPPED
    }

    @StoryType
    private var storyType = StoryType.NONE

    private var state = State.STOPPED

    private var url: String? = null

    private var storyStateListener: StoryStateListener? = null

    private val player by lazy {
        SimpleExoPlayer.Builder(context).build()
    }

    private val mediaSource by lazy {
        val sourceFactory = DefaultHttpDataSourceFactory(KEY_USER_AGENT)
        ProgressiveMediaSource.Factory(sourceFactory)
    }

    private val glide by lazy {
        Glide.with(context)
    }

    init {
        readAttributes(attrs)

        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        inflater.inflate(LAYOUT_ID, this)

        player.addListener(object : Player.EventListener {
            private var isFirstPlay = true

            override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                super.onPlayerStateChanged(playWhenReady, playbackState)
                when (playbackState) {
                    Player.STATE_ENDED -> {
                        isFirstPlay = true
                    }

                    Player.STATE_READY -> {
                        if (isFirstPlay) {
                            storyStateListener?.onReady(player.duration)
                            isFirstPlay = false
                        }
                    }

                    Player.STATE_BUFFERING -> isFirstPlay = true

                    else -> {

                    }
                }
            }
        })

        initialize()
    }

    private fun readAttributes(attributeSet: AttributeSet?) {
        val attrs = context.obtainStyledAttributes(attributeSet, R.styleable.StoryPlayerView)

        try {
            storyType = attrs.getInteger(
                R.styleable.StoryPlayerView_storyType,
                StoryType.NONE
            )

            url = attrs.getString(R.styleable.StoryPlayerView_url)
        } finally {
            attrs.recycle()
        }
    }

    private fun initialize() {
        storyPlayer.player = player
        storyPlayer.useController = false
    }

    private fun play() {
        state = State.PLAYING

        when (storyType) {
            StoryType.NONE -> {
                storyPlayer.visibility = View.GONE
                storyImageView.visibility = View.GONE
            }
            StoryType.IMAGE -> {
                storyPlayer.visibility = View.GONE
                storyImageView.visibility = View.VISIBLE

                player.stop()
                playImageStory(url)
            }
            StoryType.VIDEO -> {
                storyPlayer.visibility = View.VISIBLE
                storyImageView.visibility = View.GONE
                playVideoStory(url)
            }
        }
    }

    fun setStoryStateListener(storyStateListener: StoryStateListener) {
        this.storyStateListener = storyStateListener
    }

    private fun playVideoStory(url: String?) {
        if (url == null) return

        if (!url.endsWith(".mp4")) return

        player.prepare(mediaSource.createMediaSource(Uri.parse(url)))
        player.playWhenReady = true
    }

    private fun playImageStory(url: String?) {
        if (url == null) return

        if (!(url.startsWith("https://") || url.startsWith("https://")) &&
            !(url.endsWith(".jpeg") || url.endsWith(".jpg") || url.endsWith(".png"))
        ) return


        glide.load(url).into(storyImageView)
        storyStateListener?.onReady(DEFAULT_IMAGE_STORY_DURATION)
    }

    fun setStory(url: String, @StoryType storyType: Int) {
        this.storyType = storyType
        this.url = url
        play()
    }

    @StoryType
    fun playingStoryType(): Int {
        return storyType
    }

    fun pause() {
        if (storyType == StoryType.VIDEO)
            player.playWhenReady = false

        state = State.PAUSED
    }

    fun resume() {
        if (storyType == StoryType.VIDEO)
            player.playWhenReady = true

        state = State.PLAYING
    }

    fun stop() {
        if (storyType == StoryType.VIDEO)
            player.stop()

        state = State.STOPPED
    }

    fun isPaused() = state == State.PAUSED

    fun isPlaying() = state == State.PLAYING

    fun release() {
        player.release()
    }

    interface StoryStateListener {
        fun onReady(duration: Long)
    }
}