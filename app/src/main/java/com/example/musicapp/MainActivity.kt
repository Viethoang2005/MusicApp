package com.example.musicapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.content.ContextCompat
import com.example.musicapp.player.MusicService
import com.example.musicapp.theme.MusicAppTheme
import com.example.musicapp.ui.home.HomeScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val intent = Intent(this, MusicService::class.java)
        ContextCompat.startForegroundService(this, intent)
        setContent {
            MusicAppTheme {
                HomeScreen()
            }
        }
    }
}

