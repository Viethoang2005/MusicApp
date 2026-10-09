package com.example.musicapp.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.musicapp.R
import com.example.musicapp.data.model.Song
import com.example.musicapp.data.model.SongUIModel

@Composable
fun CardSongItem(
    songUIModel: SongUIModel,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val textColor = if(isSelected) colorResource(R.color.purple_200)
    else colorResource(R.color.textPrimary)
    Box(
        Modifier.fillMaxWidth().clickable {
            onClick()
        }.padding(vertical = 10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                modifier = Modifier.size(50.dp).clip(CircleShape),
                painter = painterResource(R.drawable.icon_music_app),
                contentDescription = "Song Image"
            )
            Spacer(Modifier.width(10.dp))
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = songUIModel.song.title,
                    fontSize = 22.sp,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.SemiBold,
                    color = textColor
                )

                Text(
                    text = songUIModel.artistName,
                    fontSize = 15.sp,
                    color = colorResource(R.color.textSecondary)
                )
            }
        }
    }

}


@Preview
@Composable
fun CardSongItemPreview() {
    CardSongItem(
        SongUIModel(
            Song("1", "Te that, anh nho em", "","", 255),
            "Thanh Hung"
        ),
        false,
        onClick = {}
    )
}