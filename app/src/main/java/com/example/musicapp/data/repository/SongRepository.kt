package com.example.musicapp.data.repository

import com.example.musicapp.data.model.Song
import com.example.musicapp.data.model.SongUIModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


class SongRepository @Inject constructor() {
    private val firestore = FirebaseFirestore.getInstance()

    suspend fun getSongs() : List<Song> {
        val snapshot = firestore
            .collection("songs")
            .get()
            .await()
        return snapshot.documents.map { document ->
            Song(
                id = document.id,
                title = document.getString("title") ?: "",
                artistId = document.getString("artistId") ?: "",
                audioUrl = document.getString("audioUrl") ?: "",
                imageUrl = document.getString("imageUrl") ?: "",
                duration = document.getLong("duration") ?: 0
            )
        }
    }

    suspend fun getArtists() : Map<String, String> {
        val snapshot = firestore
            .collection("artists")
            .get()
            .await()
        return snapshot.documents.associate { document ->
            document.id to(
                document.getString("name")?: "Unknown Artist"
            )
        }
    }

    suspend fun getSongsWithArtist() : List<SongUIModel> {
        val songs = getSongs()
        val artists = getArtists()

        return songs.map { song ->
            SongUIModel(
                song = song,
                artistName = artists[song.artistId] ?: "Unknown Artist"
            )

        }
    }
}