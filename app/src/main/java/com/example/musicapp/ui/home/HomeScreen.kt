package com.example.musicapp.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.musicapp.R

@Composable
fun HomeScreen() {
    HomeScreenContent()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenContent() {
    Scaffold(
        topBar = { TopAppBar(
            windowInsets = WindowInsets(0, 0, 0, 0),
            title = {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "MusiGo",
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
                        Column(
                            modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
                        ) {
                            Text(
                                "Recent Played",
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold,
                                color = colorResource(R.color.textPrimary)
                            )
                            Spacer(Modifier.height(12.dp))
                            LazyRow {
                                itemsIndexed(songs) { index, song ->
                                    val isSelected = currentSongId == song.song.id
                                    SongItem(
                                        song = song,
                                        isSelected,
                                        onPlay = {
                                            songViewModel.setPlaylist(songs, index)
                                        }
                                    )
                                }
                            }
                        }

                    }

                    // trending songs
                    item {
                        Spacer(Modifier.height(40.dp))
                    }
                    item {
                        Column(
                            modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
                        ) {
                            Text(
                                "Trending Songs",
                                fontSize = 22.sp,

                                fontWeight = FontWeight.Bold,
                                color = colorResource(R.color.textPrimary)
                            )
                            Spacer(Modifier.height(12.dp))
                            LazyRow {
                                itemsIndexed(songs) {index, song ->
                                    val isSelected = currentSongId == song.song.id
                                    SongItem(
                                        song = song,
                                        isSelected,
                                        onPlay = {
                                            songViewModel.setPlaylist(songs, index)
                                        }
                                    )
                                }
                            }
                        }

                    }

                    // artists
                    item {
                        Spacer(Modifier.height(40.dp))
                    }
                    item {
                        Column(
                            modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
                        ) {
                            Text(
                                "Artists",
                                fontSize = 22.sp,

                                fontWeight = FontWeight.Bold,
                                color = colorResource(R.color.textPrimary)
                            )
                            Spacer(Modifier.height(12.dp))
                            LazyRow {
                                items(artists) { artist ->
                                    ArtistItem(
                                        artist = artist
                                    )
                                }
                            }
                        }

                    }
                }
            }
        }
}

@Preview
@Composable
fun HomeScreenPreview() {
    HomeScreenContent()
}