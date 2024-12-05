package com.example.gyanchakkhu.utils

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gyanchakkhu.ui.theme.Blue40
import com.example.gyanchakkhu.ui.theme.Blue80
import com.example.gyanchakkhu.ui.theme.Purple20

@Composable
fun BookDetails(
    modifier: Modifier = Modifier,
    actionLabel: String
) {
    var show by remember { mutableStateOf(false)}
    Column(
        modifier = modifier
            .padding(24.dp)
            .clip(RoundedCornerShape(8.dp))
            .border(1.dp, Color.Black, RoundedCornerShape(8.dp))
            .background(Color.White)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Book Details",
            modifier = Modifier.padding(top = 16.dp),
            color = Purple20,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Column(
                modifier = modifier
                    .padding(start = 24.dp, end = 24.dp, top = 16.dp)
                    .background(Color.White)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row {
                    Column(
                        modifier = Modifier.padding(horizontal = 12.dp),
                        verticalArrangement = Arrangement.spacedBy(9.dp),
                    ) {
                        listOf(
                            "Book Name",
                            "Book ID",
                            "Library Section",
                            "Rack No."
                        ).forEach {
                            Text(text = it)
                        }
                    }
                    Column(
                        modifier = Modifier.padding(horizontal = 12.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        listOf(
                            "                                                               ",
                            "                                                               ",
                            "                                                               ",
                            "                                                               "
                        ).forEach {
                            Text(
                                text = it,
                                modifier = Modifier
                                    .border(
                                        shape = RoundedCornerShape(12.dp),
                                        width = 1.dp,
                                        color = Color.Black
                                    )
                                    .padding(horizontal = 8.dp, vertical = 2.dp),
                                fontSize = 10.sp
                            )
                        }
                    }
                }
                Button(
                    modifier = Modifier
                        .padding(16.dp)
                        .height(28.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Blue40),
                    contentPadding = PaddingValues(horizontal = 44.dp, vertical = 1.dp),
                    onClick = {
                        /*TODO*/
                        show = false
                    }
                ) {
                    Text(
                        text = actionLabel,
                        color = Color.White,
                        fontSize = 22.sp
                    )
                }
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
                    TextButton(onClick = { show = true }) {
                        Text(
                            text = "Issue your virtual library card",
                            color = Blue80,
                            fontSize = 18.sp
                        )
                    }
                }
            }
        }
    }
}