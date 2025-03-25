package com.example.gyanchakkhu.utils

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gyanchakkhu.R
import com.example.gyanchakkhu.ui.theme.Blue80
import com.example.gyanchakkhu.ui.theme.poppinsFontFamily

@Composable
fun DigitalLibraryCard(
    name: String,
    cardIssueNumber: String,
    libraryName: String,
    libraryUid: String,
    isUserEnrolledInLibrary: Boolean
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
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
                    .padding(vertical = 20.dp, horizontal = 24.dp),
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
                            style = TextStyle(
                                fontFamily = poppinsFontFamily
                            ),
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = cardIssueNumber,
                            fontSize = 28.sp,
                            style = TextStyle(
                                fontFamily = poppinsFontFamily
                            ),
                            color = Color.Gray,
                            modifier = Modifier.height(30.dp)
                        )
                        Text(
                            text = "card-issue-number",
                            color = Color.Gray,
                            style = TextStyle(
                                lineHeight = 1.sp,
                                fontFamily = poppinsFontFamily
                            )
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    Image(
                        imageVector = ImageVector.vectorResource(id = R.drawable.scholar_owl),
                        modifier = Modifier
                            .size(36.dp),
                        contentDescription = "Scholar Owl",
                        colorFilter = ColorFilter.tint(Color.Black)
                    )
                }
                Spacer(modifier = Modifier.height(60.dp))
                Text(
                    text = "Library Name : $libraryName",
                    color = Color.Gray,
                    style = TextStyle(
                        fontFamily = poppinsFontFamily
                    )
                )
                Text(
                    text = "Library UID : $libraryUid",
                    color = Color.Gray,
                    style = TextStyle(
                        fontFamily = poppinsFontFamily
                    )
                )
            }
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(if (!isUserEnrolledInLibrary) Color.White else Color.Transparent),
                contentAlignment = Alignment.Center
            ) {
                androidx.compose.animation.AnimatedVisibility(
                    visible = !isUserEnrolledInLibrary,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    Text(
                        text = "Please enroll in a library",
                        color = Blue80,
                        style = TextStyle(
                            fontFamily = poppinsFontFamily
                        ),
                        fontSize = 18.sp
                    )
                }
            }
        }
    }
}