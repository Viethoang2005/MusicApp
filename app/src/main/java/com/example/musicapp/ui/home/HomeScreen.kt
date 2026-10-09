package com.example.musicapp.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.musicapp.PlayerState
import com.example.musicapp.R
import com.example.musicapp.SongViewModel
import com.example.musicapp.components.CardSongItem
import com.example.musicapp.data.model.SongUIModel

@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel = hiltViewModel(),
    songViewModel: SongViewModel = hiltViewModel()
) {

    val songs by homeViewModel.songs.collectAsState()
    val playerState by songViewModel.playerState.collectAsState()

    LaunchedEffect(Unit) {
        homeViewModel.loadSongs()
    }
    HomeScreenContent(
        songs = songs,
        playerState = playerState,
        onSongClick = { list, index ->
            songViewModel.setPlaylist(list, index)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenContent(
    songs: List<SongUIModel>,
    playerState: PlayerState,
    onSongClick: (List<SongUIModel>, Int) -> Unit
) {
    Scaffold(
        topBar = { TopAppBar(
            windowInsets = WindowInsets(0, 0, 0, 0),
            title = {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "Music App",
                        fontSize = 24.sp,
                        textAlign = TextAlign.Center,
                        color = colorResource(R.color.textPrimary)
                    )
                }
            },
            navigationIcon = {
                Image(
                    painter = painterResource(R.drawable.icon_music_app),
                    contentDescription = null,
                    modifier = Modifier.padding(start = 12.dp).size(36.dp)
                )
            },
            actions =  {
                Box(Modifier.size(48.dp))
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = colorResource(R.color.primaryDark)
            )
        ) }
    ) { innerPadding ->
            if(songs.isEmpty()) {
                Box(Modifier.fillMaxSize().padding(innerPadding).background(colorResource(R.color.primaryDark)),
                    contentAlignment = Alignment.Center) {
                    Text(
                        text = "Khong tim thay nhac",
                        fontSize = 25.sp,
                        color = colorResource(R.color.textPrimary)
                    )
                }
            }else {
                Box(
                    Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .background(colorResource(R.color.primaryDark))
                ) {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize().padding(16.dp)
                    ) {
                        item {
                            Text(
                                "List Musics",
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold,
                                color = colorResource(R.color.textPrimary)
                            )
                        }
                        itemsIndexed(songs) { index, song ->
                            val isSelected = playerState.currentSongId == song.song.id
                            CardSongItem(
                                songUIModel = song,
                                isSelected = isSelected,
                                onClick = {
                                    onSongClick(songs, index)
                                }
                            )
                        }
                    }
                }
            }
        }
}

@Preview
@Composable
fun HomeScreenPreview() {
    HomeScreenContent(
        songs = emptyList(),
        playerState = PlayerState(),
        onSongClick = {_, _ -> }
    )
}