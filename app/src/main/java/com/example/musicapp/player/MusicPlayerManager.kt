package com.example.musicapp.player

import android.content.ComponentName
import android.content.Context
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.google.common.util.concurrent.ListenableFuture
import dagger.hilt.android.UnstableApi
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@UnstableApi
@Singleton
class MusicPlayerManager @Inject constructor(
    @ApplicationContext context: Context
)
{
    private val appContext = context.applicationContext

    private val mainExecutor = ContextCompat.getMainExecutor(appContext)
    var onSongEnded: (() -> Unit)? = null

    private var mediaControllerFuture: ListenableFuture<MediaController>? = null
    private var controller: MediaController? = null

    private fun getOrCreateControllerFuture():
            ListenableFuture<MediaController> {

        synchronized(this) {
            mediaControllerFuture?.let {
                return it
            }

            val future = MediaController.Builder(
                appContext,
                SessionToken(
                    appContext,
                    ComponentName(
                        appContext,
                        MusicService::class.java
                    )
                )
            ).buildAsync()

            mediaControllerFuture = future

            future.addListener(
                {
                    try {
                        val mediaController = future.get()
                        controller = mediaController
                        mediaController.addListener(playerListener)
                    } catch (e: Exception) {
                        Log.e(
                            "MusicPlayerManager",
                            "Không thể kết nối MusicService",
                            e
                        )
                    }
                },
                mainExecutor
            )

            return future
        }
    }

    private val playerListener = object : Player.Listener {

        override fun onPlayerError(error: PlaybackException) {
            Log.e(
                "ExoPlayerError",
                "Lỗi phát nhạc: ${error.errorCodeName} - ${error.message}"
            )
        }

        override fun onPlaybackStateChanged(playbackState: Int) {
            when (playbackState) {
                Player.STATE_IDLE ->
                    Log.d("ExoPlayerState", "STATE_IDLE")

                Player.STATE_BUFFERING ->
                    Log.d("ExoPlayerState", "STATE_BUFFERING")

                Player.STATE_READY ->
                    Log.d("ExoPlayerState", "STATE_READY")

                Player.STATE_ENDED -> {
                    Log.d("ExoPlayerState", "STATE_ENDED")
                    onSongEnded?.invoke()
                }
            }
        }
    }
    init {
        getOrCreateControllerFuture()
    }

    private fun withController(
        createIfNeeded: Boolean = true,
        action: (MediaController) -> Unit
    ) {
        val future = if (createIfNeeded) {
            getOrCreateControllerFuture()
        } else {
            synchronized(this) {
                mediaControllerFuture
            }
        } ?: return

        future.addListener(
            {
                try {
                    action(future.get())
                } catch (e: Exception) {
                    Log.e(
                        "MusicPlayerManager",
                        "Lỗi điều khiển player",
                        e
                    )
                }
            },
            mainExecutor
        )
    }
    val isPlaying : Boolean
        get() = controller?.isPlaying ?: false

    val currentPosition: Long
        get() = controller?.currentPosition ?: 0L

    val duration: Long
        get() = (controller?.duration ?: 0L).coerceAtLeast(0L)

    fun play(
        url: String,
        title: String = "",
        artist: String = ""
    ) {
        if (url.isBlank()) return

        withController { player ->

            val metadata = MediaMetadata.Builder()
                .setTitle(title)
                .setArtist(artist)
                .build()

            val mediaItem = MediaItem.Builder()
                .setUri(url)
                .setMediaMetadata(metadata)
                .build()

            player.setMediaItem(mediaItem)
            player.prepare()
            player.play()
        }
    }


    //    fun pause() {
//        player.pause()
//    }
//    fun resume() {
//        player.play()
//    }
//
//    fun stop() {
//        player.stop()
//    }
//    fun release() {
//        player.release()
//    }
    fun seekTo(position: Long) {
        withController { player ->
            player.seekTo(position)
        }
    }

    fun togglePlayPause() {
        withController { player ->
            if (player.isPlaying) {
                player.pause()
            } else {
                if (player.playbackState == Player.STATE_ENDED) {
                    player.seekTo(0L)
                }

                player.play()
            }
        }
    }
}