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
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gyanchakkhu.ui.theme.Blue40
import com.example.gyanchakkhu.ui.theme.Blue80
import com.example.gyanchakkhu.ui.theme.MyPurple120
import com.example.gyanchakkhu.ui.theme.MyPurple40
import com.example.gyanchakkhu.ui.theme.MyPurple80
import com.example.gyanchakkhu.ui.theme.Purple20

@Composable
fun BookDetailsInIssueAndSubmit(
    modifier: Modifier = Modifier,
    actionLabel: String,
    bookName: String = "",
    bookId: String = "",
    librarySection: String = "",
    rackNo: String = ""
) {
    var show by remember { mutableStateOf(false) }
    Column(
        modifier = modifier
            .padding(24.dp)
            .clip(RoundedCornerShape(20.dp))
            .border(1.dp, Color.Black, RoundedCornerShape(20.dp))
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
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),
                ) {
                    Text(
                        text = "Book Name",
                        color = MyPurple40,
                        modifier = Modifier.weight(2f)
                    )
                    Text(
                        text = bookName,
                        modifier = Modifier
                            .border(1.dp, Color.Black, RoundedCornerShape(12.dp))
                            .padding(horizontal = 12.dp)
                            .weight(3f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),
                ) {
                    Text(
                        text = "Book ID",
                        color = MyPurple40,
                        modifier = Modifier.weight(2f)
                    )
                    Text(
                        text = bookId,
                        modifier = Modifier
                            .border(1.dp, Color.Black, RoundedCornerShape(12.dp))
                            .padding(horizontal = 12.dp)
                            .weight(3f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),
                ) {
                    Text(
                        text = "Library Section",
                        color = MyPurple40,
                        modifier = Modifier.weight(2f)
                    )
                    Text(
                        text = librarySection,
                        modifier = Modifier
                            .border(1.dp, Color.Black, RoundedCornerShape(12.dp))
                            .padding(horizontal = 12.dp)
                            .weight(3f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),
                ) {
                    Text(
                        text = "Rack No.",
                        color = MyPurple40,
                        modifier = Modifier.weight(2f)
                    )
                    Text(
                        text = rackNo,
                        modifier = Modifier
                            .border(1.dp, Color.Black, RoundedCornerShape(12.dp))
                            .padding(horizontal = 12.dp)
                            .weight(3f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
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

@Composable
fun BookDetailsInSearch(
    modifier: Modifier = Modifier,
    bookName: String = "",
    bookId: String = "",
    librarySection: String = "",
    rackNo: String = ""
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .border(1.dp, Color.Black, RoundedCornerShape(20.dp))
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = bookName,
            color = MyPurple80,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(16.dp)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 20.dp, bottom = 12.dp),
        ) {
            Text(
                text = "Book ID",
                color = MyPurple80,
                fontSize = 18.sp,
                fontWeight = FontWeight.Normal,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = bookId,
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(MyPurple120)
                    .padding(horizontal = 12.dp)
                    .weight(1f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 20.dp, bottom = 12.dp),
        ) {
            Text(
                text = "Library Section",
                color = MyPurple80,
                fontSize = 18.sp,
                fontWeight = FontWeight.Normal,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = librarySection,
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(MyPurple120)
                    .padding(horizontal = 12.dp)
                    .weight(1f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 20.dp, bottom = 12.dp),
        ) {
            Text(
                text = "Rack No.",
                color = MyPurple80,
                fontSize = 18.sp,
                fontWeight = FontWeight.Normal,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = rackNo,
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(MyPurple120)
                    .padding(horizontal = 12.dp)
                    .weight(1f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        Spacer(modifier = Modifier.height(6.dp))
    }
}

@Composable
fun BookDetailsInHistory(
    modifier: Modifier = Modifier,
    bookName: String = "",
    bookId: String = "",
    issueDate: String = "",
    submitDate: String = ""
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .border(1.dp, Color.Black, RoundedCornerShape(20.dp))
            .background(Color.White)
            .padding(20.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = bookName,
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.align(Alignment.Start)
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = bookId,
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.align(Alignment.Start)
        )
        Spacer(modifier = Modifier
            .height(20.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
            ) {
                Text(
                    text = "Issue Date",
                )
                Text(
                    text = issueDate
                )
            }
            Column(
                modifier = Modifier
                    .weight(1f)
            ) {
                Text(
                    text = "Submit Date",
                    modifier = Modifier.align(Alignment.End)
                )
                Text(
                    text = submitDate,
                    modifier = Modifier.align(Alignment.End)
                )
            }
        }
    }
}