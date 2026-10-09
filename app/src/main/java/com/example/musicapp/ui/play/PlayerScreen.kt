package com.example.musicapp.ui.play

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.RepeatOne
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.musicapp.R
import com.example.musicapp.RepeatMode
import com.example.musicapp.SongViewModel
import com.example.musicapp.data.model.Song
import com.example.musicapp.data.model.SongUIModel

@Composable
fun PlayerScreen(
    songViewModel: SongViewModel = hiltViewModel(),
    onBackClick: () -> Unit
) {
    val playerState by songViewModel.playerState.collectAsState()
    val song = playerState.currentSong
    PlayerScreenContent(
        song = song,
        isPlaying = playerState.isPlaying,
        currentPosition = playerState.currentPosition,
        songDuration = playerState.duration,
        repeatMode = playerState.repeatMode,
        onSeek = { newValue ->
            val targetPosition  = (newValue * playerState.duration).toLong()
            songViewModel.seekTo(targetPosition)
        },
        formatDuration = { ms -> songViewModel.formatDuration(ms)},
        onPlayPauseClick =  { songViewModel.togglePlayPause() },
        onPreviousClick = { songViewModel.playPrevious() },
        onNextClick = { songViewModel.playNext() },
        toggleRepeat = { songViewModel.toggleRepeatMode() },
        onBackClick = onBackClick
    )
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerScreenContent(
    song: SongUIModel?,
    isPlaying: Boolean,
    currentPosition: Long,
    songDuration: Long,
    repeatMode: RepeatMode,
    onSeek: (Float) -> Unit,
    formatDuration: (Long) -> String,
    onPlayPauseClick: () -> Unit,
    onPreviousClick: () -> Unit,
    onNextClick: () -> Unit,
    toggleRepeat: () -> Unit,
    onBackClick: () -> Unit
) {
    val calculatePosition = if(songDuration > 0) {
        currentPosition.toFloat() / songDuration.toFloat()
    } else 0f

    var slidePosition by remember { mutableStateOf<Float?>(null) }
    val currentSliderValue = slidePosition ?: calculatePosition
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "MusiApp",
                            fontSize = 24.sp,
                            textAlign = TextAlign.Center,
                            color = colorResource(R.color.textPrimary)
                        )
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            Log.d("PlayerScreen", "Back button clicked")
                            onBackClick()
                        },
                        colors = IconButtonDefaults.iconButtonColors(
                            contentColor = Color.White
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBackIosNew,
                            contentDescription = null
                        )
                    }
                },
                actions =  {
                    Box(Modifier.size(48.dp))
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colorResource(R.color.primaryDark)
                )
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(colorResource(R.color.primaryDark))
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(400.dp)
                        .padding(20.dp)
                        .clip(RoundedCornerShape(40.dp)),
                    painter = painterResource(R.drawable.icon_music_app),
                    contentDescription = null
                )
                Spacer(Modifier.height(10.dp))
                Text(
                    text = song?.song?.title ?: "Unknown Title",
                    fontSize = 27.sp,
                    fontWeight = FontWeight.Bold,
                    overflow = TextOverflow.Ellipsis,
                    color = colorResource(R.color.textPrimary)
                )
                Spacer(Modifier.height(20.dp))
                Text(
                    text = song?.artistName ?: "Unknown Artist",
                    fontSize = 20.sp,
                    color = colorResource(R.color.textSecondary)
                )

                Spacer(Modifier.height(40.dp))
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                ) {
                    Slider(
                        value = currentSliderValue,
                        onValueChange = {newValue ->
                            slidePosition = newValue
                        },
                        onValueChangeFinished = {
                            slidePosition?.let { finalValue ->
                                onSeek(finalValue)
                            }
                            slidePosition = null
                        },
                        colors = SliderDefaults.colors(
                            thumbColor = colorResource(R.color.textPrimary),
                            activeTrackColor = colorResource(R.color.textPrimary),
                            inactiveTrackColor = Color.Gray.copy(alpha = 0.4f)
                        ),
                        thumb = {
                            Box(
                                modifier = Modifier
                                    .size(12.dp)
                                    .clip(CircleShape)
                                    .background(colorResource(R.color.textPrimary))

                            )
                        }
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = formatDuration(currentPosition),
                            fontSize = 13.sp,
                            color = colorResource(R.color.textSecondary)
                        )
                        Text(
                            text = formatDuration(songDuration),
                            fontSize = 13.sp,
                            color = colorResource(R.color.textSecondary)
                        )
                    }
                }

                Spacer(Modifier.height(20.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(Modifier.size(40.dp))
                    IconButton(
                        onClick = onPreviousClick,
                        colors = IconButtonDefaults.iconButtonColors(
                            contentColor = colorResource(R.color.textPrimary)
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.SkipPrevious,
                            contentDescription = "Previous"
                        )
                    }
                    FloatingActionButton(
                        onClick = onPlayPauseClick,
                        containerColor = colorResource(R.color.textPrimary),
                        contentColor = colorResource(R.color.primaryDark),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(
                            imageVector = if(isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                            contentDescription = if(isPlaying) "Pause" else "Play",
                            modifier = Modifier.size(32.dp)
                        )
                    }
                    IconButton(
                        onClick = onNextClick,
                        colors = IconButtonDefaults.iconButtonColors(
                            contentColor = colorResource(R.color.textPrimary)
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.SkipNext,
                            contentDescription = "Next"
                        )
                    }
                    IconButton(
                        onClick = toggleRepeat,
                        colors = IconButtonDefaults.iconButtonColors(
                            contentColor = colorResource(R.color.textPrimary)
                        )
                    ) {
                        val (icon, tint) = when(repeatMode) {
                            RepeatMode.ONE -> Icons.Default.RepeatOne to colorResource(R.color.textPrimary)
                            RepeatMode.ALL -> Icons.Default.Repeat to colorResource(R.color.textPrimary)
                            RepeatMode.OFF -> Icons.Default.Repeat to colorResource(R.color.textSecondary)
                        }
                        Icon(
                            imageVector = icon,
                            contentDescription = "Repeat",
                            tint = tint
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun PLayerScreenPreview() {
    val song = Song("1", "Te that, anh nho em", "Thanh Hung", "",  225)
    val songModel = SongUIModel(song, "Thanh Hưng")
    PlayerScreenContent(
        songModel,
        false,
        45000L,
        songDuration = 225000L,
        repeatMode = RepeatMode.OFF,
        onSeek = {},
        formatDuration = {"1:15"},
        onPlayPauseClick = {},
        onNextClick = {},
        onPreviousClick = {},
        toggleRepeat = {},
        onBackClick = {}
    )
}