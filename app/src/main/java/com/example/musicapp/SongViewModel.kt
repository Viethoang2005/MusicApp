package com.example.musicapp

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicapp.data.model.SongUIModel
import com.example.musicapp.player.MusicPlayerManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

enum class RepeatMode {
    OFF, ONE, ALL
}
data class PlayerState(
    val currentSong: SongUIModel? = null,
    val isPlaying: Boolean = false,
    var currentPosition: Long = 0L,
    val duration: Long = 0L,
    val currentSongId: String? = null,
    val playlist: List<SongUIModel> = emptyList(),
    val currentIndex: Int = -1,
    val repeatMode: RepeatMode = RepeatMode.OFF
)
@HiltViewModel
class SongViewModel @Inject constructor(
    private val musicPlayerManager: MusicPlayerManager
): ViewModel() {

    private val _playerState = MutableStateFlow(PlayerState())
    val playerState = _playerState.asStateFlow()

    init {
        musicPlayerManager.onSongEnded = {
            playNext()
        }
        viewModelScope.launch {
            while (true) {
                if(_playerState.value.currentSong != null) {
                    _playerState.value = _playerState.value.copy(
                        currentPosition = musicPlayerManager.currentPosition,
                        duration = musicPlayerManager.duration.coerceAtLeast(0L),
                        isPlaying = musicPlayerManager.isPlaying
                    )
                }
                delay(500L.milliseconds)
            }
        }
    }

    fun setPlaylist(songs: List<SongUIModel>, initialIndex: Int) {
        if(songs.isEmpty()) return
        val safeIndex = initialIndex.coerceIn(0, songs.size - 1)
        _playerState.value = _playerState.value.copy(
            playlist = songs,
            currentIndex = safeIndex
        )
        playSong(songs[safeIndex])
    }

    fun seekTo(position: Long) {
        musicPlayerManager.seekTo(position)
        _playerState.value.currentPosition = position
    }

    fun playSong(song: SongUIModel) {
        val currentList = _playerState.value.playlist
        val index = currentList.indexOfFirst { it.song.id == song.song.id }

        musicPlayerManager.play(song.song.audioUrl)
        _playerState.value = _playerState.value.copy(
            currentSong = song,
            currentSongId = song.song.id,
            isPlaying = true,
            duration = musicPlayerManager.duration.coerceAtLeast(0L),
            currentIndex = if(index != -1) index else _playerState.value.currentIndex
        )
    }

    fun togglePlayPause() {
        musicPlayerManager.togglePlayPause()
        _playerState.value = _playerState.value.copy(
            isPlaying = musicPlayerManager.isPlaying
        )
    }

    fun toggleRepeatMode() {
        val currentMode = _playerState.value.repeatMode
        val newMode = when(currentMode) {
            RepeatMode.OFF -> RepeatMode.ALL
            RepeatMode.ALL -> RepeatMode.ONE
            RepeatMode.ONE -> RepeatMode.OFF
        }
        _playerState.value = _playerState.value.copy(repeatMode = newMode )
    }
    fun playNext() {
        val state = _playerState.value
        if(state.playlist.isEmpty()) return

        if(state.repeatMode == RepeatMode.ONE && state.currentSong != null) {
            playSong(state.currentSong)
            return
        }
        val nextIndex = (state.currentIndex + 1) % state.playlist.size
        if(nextIndex == 0 && state.repeatMode == RepeatMode.OFF) return
        setPlaylist(state.playlist, nextIndex)
    }

    fun playPrevious() {
        val state = _playerState.value
        if(state.playlist.isEmpty()) return

        val prevIndex = if(state.currentIndex - 1 < 0) state.playlist.size - 1 else state.currentIndex - 1
        setPlaylist(state.playlist, prevIndex)
    }

    @SuppressLint("DefaultLocale")
    fun formatDuration(ms: Long) : String {
        if(ms < 0) return "0:00"
        val totalSeconds = ms/1000
        val minutes = totalSeconds / 60
        val seconds = totalSeconds % 60
        return String.format("%d:%02d", minutes, seconds)
    }
}