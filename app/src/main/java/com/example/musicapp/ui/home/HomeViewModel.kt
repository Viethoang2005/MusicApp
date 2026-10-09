package com.example.musicapp.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicapp.data.model.SongUIModel
import com.example.musicapp.data.repository.SongRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val songRepository: SongRepository
): ViewModel() {
    private val _songs = MutableStateFlow<List<SongUIModel>>(emptyList())
    val songs: StateFlow<List<SongUIModel>> = _songs.asStateFlow()

    fun loadSongs() {
        viewModelScope.launch {
            try {
                Log.d("RepoDebug", "Bắt đầu gọi dữ liệu...")
                val res = songRepository.getSongsWithArtist()
                Log.d("Check Data", "Tải thành công! So luong nhac tai duoc: ${res.size}")
                _songs.value = res
            }catch (e: Exception) {
                e.printStackTrace()
                Log.e("RepoDebug", "Lỗi ngoại lệ khi tải: ${e.message}")
            }
        }
    }
}