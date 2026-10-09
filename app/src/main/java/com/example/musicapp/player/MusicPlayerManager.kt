package com.example.musicapp.player

import android.content.Context
import android.util.Log
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.compose.material3.Player
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MusicPlayerManager @Inject constructor(
    @ApplicationContext context: Context
)
{

    private val player = ExoPlayer.Builder(context)
        .setAudioAttributes(
            AudioAttributes.Builder()
                .setContentType(C.AUDIO_CONTENT_TYPE_MUSIC)
                .setUsage(C.USAGE_MEDIA)
                .build(),
            true
        )
        .build()
        .apply {
            addListener(object : Player.Listener {
                override fun onPlayerError(error: PlaybackException) {
                    super.onPlayerError(error)
                    Log.e("ExoPlayerError", "Lỗi phát nhạc: ${error.errorCodeName} - ${error.message}")
                }

                override fun onPlaybackStateChanged(playbackState: Int) {
                    super.onPlaybackStateChanged(playbackState)
                    when (playbackState) {
                        Player.STATE_IDLE -> Log.d("ExoPlayerState", "STATE_IDLE")
                        Player.STATE_BUFFERING -> Log.d("ExoPlayerState", "STATE_BUFFERING")
                        Player.STATE_READY -> Log.d("ExoPlayerState", "STATE_READY")
                        Player.STATE_ENDED -> Log.d("ExoPlayerState", "STATE_ENDED")
                    }
                }
            })
        }

    var onSongEnded: (() -> Unit)? = null
    init {
        player.addListener(object: Player.Listener{
            override fun onPlaybackStateChanged(playbackState: Int) {
                if(playbackState == Player.STATE_ENDED) onSongEnded?.invoke()
            }
        })
    }
    val isPlaying : Boolean
        get() = player.isPlaying

    val currentPosition : Long
        get() = player.currentPosition

    val duration: Long
        get() = player.duration.coerceAtLeast(0L)

    fun play(url: String) {
        val mediaItem = MediaItem.fromUri(url)
        player.setMediaItem(mediaItem)
        player.prepare()
        player.play()
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
        player.seekTo(position)
    }

    fun togglePlayPause() {
        if(player.isPlaying) player.pause()
        else {
            if(player.playbackState == Player.STATE_ENDED) player.seekTo(0)
            player.play()
        }

    }
}