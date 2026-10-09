package com.example.musicapp.player

import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.UnstableApi
import javax.inject.Inject

@UnstableApi
class MusicService : MediaSessionService(){
    private var mediaSession: MediaSession? = null


    override fun onCreate() {
        super.onCreate()
        val player = ExoPlayer.Builder(this).build()
        val audioAttributes = AudioAttributes.Builder()
            .setContentType(C.AUDIO_CONTENT_TYPE_MUSIC)
            .setUsage(C.USAGE_MEDIA)
            .build()

        player.setAudioAttributes(audioAttributes, true)
        mediaSession = MediaSession.Builder(this, player).build()
    }

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo) : MediaSession? {
        return mediaSession
    }

    override fun onDestroy() {
        val session = mediaSession
        mediaSession = null

        session?.player?.release()
        session?.release()
        super.onDestroy()
    }
}