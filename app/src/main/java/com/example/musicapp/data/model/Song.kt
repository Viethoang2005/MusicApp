package com.example.musicapp.data.model

data class Song(
    val id: String,
    val title: String,
    val artistId: String,
    val audioUrl: String,
    val duration: Long
)
