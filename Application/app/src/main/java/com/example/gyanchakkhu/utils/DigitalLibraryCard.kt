package com.example.gyanchakkhu.utils

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gyanchakkhu.R
import com.example.gyanchakkhu.ui.theme.Blue80

@Composable
fun DigitalLibraryCard(
    name: String,
    cardIssueNumber: String,
    libraryName: String,
    libraryUid: String,
    showCard: Boolean,
    message: String,
    modifier: Modifier = Modifier
) {
    var show by remember { mutableStateOf(showCard) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White.copy(alpha = 0.6f))
            .border(1.dp, Color.Black, RoundedCornerShape(20.dp))
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp, horizontal = 30.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column {
                        Text(
                            text = name,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = cardIssueNumber,
                            fontSize = 28.sp,
                            color = Color.Gray,
                            modifier = Modifier.height(28.dp)
                        )
                        Text(
                            text = "card-issue-number",
                            color = Color.Gray,
                            style = TextStyle(lineHeight = 1.sp)
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    Image(
                        painter = painterResource(id = R.drawable.scholar_owl),
                        modifier = Modifier
                            .size(36.dp),
                        contentDescription = "Scholar Owl"
                    )
                }
                Spacer(modifier = Modifier.height(60.dp))
                Text(
                    text = "Library Name: $libraryName",
                    color = Color.Gray
                )
                Text(
                    text = "Library UID : $libraryUid",
                    color = Color.Gray
                )
            }

            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(if (!show) Color.White else Color.Transparent),
                contentAlignment = Alignment.Center
            ) {
                androidx.compose.animation.AnimatedVisibility(
                    visible = !show,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    TextButton(onClick = { if(showCard) { show = true } else Unit }) {
                        Text(
                            text = message,
                            color = Blue80,
                            fontSize = 18.sp
                        )
                    }
                }
            }
        }
    }
}