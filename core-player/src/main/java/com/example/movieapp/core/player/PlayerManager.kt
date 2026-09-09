package com.example.movieapp.core.player

import android.content.Context
import androidx.annotation.OptIn
import androidx.media3.common.C
import androidx.media3.common.MediaItem

import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.trackselection.DefaultTrackSelector
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

/** Lifecycle-aware wrapper around Media3 ExoPlayer. */
@OptIn(UnstableApi::class)
class PlayerManager(
    context: Context,
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.Main.immediate),
) {
    private val applicationContext = context.applicationContext
    private val trackSelector = DefaultTrackSelector(applicationContext)
    private val player = ExoPlayer.Builder(applicationContext)
        .setTrackSelector(trackSelector)
        .build()
    private val _currentPosition = MutableStateFlow(0L)
    private var positionJob: Job? = null

    /** Emits the current playback position approximately once per second. */
    val currentPosition: Flow<Long> = _currentPosition.asStateFlow()

    /** The underlying player for binding to a Media3 PlayerView. */
    val exoPlayer: ExoPlayer = player

    /** Prepares an HLS, DASH, or progressive manifest and seeks to the resume position. */
    fun prepare(manifestUrl: String, licenseUrl: String?, startPositionMs: Long) {
        val drmConfiguration = licenseUrl?.let {
            MediaItem.DrmConfiguration.Builder(C.WIDEVINE_UUID)
                .setLicenseUri(it)
                .build()
        }
        player.setMediaItem(
            MediaItem.Builder()
                .setUri(manifestUrl)
                .setDrmConfiguration(drmConfiguration)
                .build(),
        )
        player.seekTo(startPositionMs.coerceAtLeast(0L))
        player.prepare()
        startPositionUpdates()
    }

    /** Starts playback. */
    fun play() = player.play()

    /** Pauses playback. */
    fun pause() = player.pause()

    /** Seeks to a position in milliseconds. */
    fun seekTo(positionMs: Long) = player.seekTo(positionMs.coerceAtLeast(0L))

    /** Sets the playback speed. */
    fun setPlaybackSpeed(speed: Float) {
        require(speed > 0f) { "Playback speed must be greater than zero" }
        player.setPlaybackSpeed(speed)
    }

    /** Limits video tracks to the requested maximum height, such as 720 or 1080. */
    fun selectMaxVideoHeight(maxHeight: Int) {
        require(maxHeight > 0) { "Max video height must be greater than zero" }
        trackSelector.setParameters(
            trackSelector.buildUponParameters()
                .setMaxVideoSize(maxHeight, Int.MAX_VALUE)
                .build(),
        )
    }

    /** Releases the player and its position polling job. Call from Compose `onDispose`. */
    fun release() {
        positionJob?.cancel()
        player.release()
    }

    private fun startPositionUpdates() {
        if (positionJob?.isActive == true) return
        positionJob = scope.launch {
            while (isActive) {
                _currentPosition.value = player.currentPosition
                delay(1_000)
            }
        }
    }
}
