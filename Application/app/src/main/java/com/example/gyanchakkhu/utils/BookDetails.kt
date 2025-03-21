package com.example.gyanchakkhu.utils

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.sp
import com.example.gyanchakkhu.R
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
    rackNo: String = "",
    onClick: () -> Unit,
    isVisible: Boolean = false
) {
    val bookDetails = listOf(
        "Book Name" to bookName,
        "Book ID" to bookId,
        "Library Section" to librarySection,
        "Rack No." to rackNo
    )
    Column(
        modifier = modifier
            .padding(16.dp)
            .clip(RoundedCornerShape(20.dp))
            .border(1.dp, Color.Black, RoundedCornerShape(20.dp))
            .background(Color.White)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (isVisible) {
            Text(
                text = "Book Details",
                modifier = Modifier.padding(top = 16.dp),
                color = Purple20,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }
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
                bookDetails.forEach { (label, value) ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp),
                    ) {
                        Text(
                            text = label,
                            color = MyPurple40,
                            modifier = Modifier.weight(2f)
                        )
                        Text(
                            text = value,
                            modifier = Modifier
                                .border(1.dp, Color.Black, RoundedCornerShape(12.dp))
                                .padding(horizontal = 12.dp)
                                .horizontalScroll(rememberScrollState())
                                .weight(3f),
                            maxLines = 1,
                            overflow = TextOverflow.Clip
                        )
                    }
                }
                Button(
                    modifier = Modifier
                        .padding(16.dp)
                        .height(28.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Blue40),
                    contentPadding = PaddingValues(horizontal = 44.dp, vertical = 1.dp),
                    onClick = {
                        onClick()
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
                    .background(if (!isVisible) Color.White else Color.Transparent),
                contentAlignment = Alignment.Center
            ) {
                androidx.compose.animation.AnimatedVisibility(
                    visible = !isVisible,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    Text(
                        text = stringResource(R.string.scan_qr_message),
                        color = Blue80,
                        fontSize = 18.sp

                    )

                }
            }
            if (!isVisible) {
                Text(
                    text = "Book Details",
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .align(Alignment.TopCenter),
                    color = Purple20,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
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
            color = MyPurple80,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
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
                    .weight(2f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        Spacer(
            modifier = Modifier
                .height(20.dp)
        )
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
                    color = MyPurple80,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Normal,
                )
                Text(
                    text = issueDate,
                    color = MyPurple80,
                    fontWeight = FontWeight.Normal,
                )
            }
            Column(
                modifier = Modifier
                    .weight(1f)
            ) {
                Text(
                    text = "Submit Date",
                    color = MyPurple80,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Normal,
                    modifier = Modifier.align(Alignment.End)
                )
                Text(
                    text = submitDate,
                    color = MyPurple80,
                    fontWeight = FontWeight.Normal,
                    modifier = Modifier.align(Alignment.End)
                )
            }
        }
    }
}

@Composable
fun RecentIssuesInHome(
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
        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = "Book Name",
                color = MyPurple80,
                fontSize = 18.sp,
                fontWeight = FontWeight.Normal,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = bookName,
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(MyPurple120)
                    .padding(horizontal = 12.dp)
                    .weight(2f)
                    .horizontalScroll(rememberScrollState()),
                maxLines = 1,
                overflow = TextOverflow.Clip
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
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
                    .weight(2f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "Issue Date",
                    color = MyPurple80,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Normal
                )
                Text(
                    text = issueDate,
                    color = MyPurple80,
                    fontWeight = FontWeight.Normal,
                )
            }
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "Submit Date",
                    color = MyPurple80,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Normal,
                    modifier = Modifier.align(Alignment.End)
                )
                Text(
                    text = submitDate,
                    color = MyPurple80,
                    fontWeight = FontWeight.Normal,
                    modifier = Modifier.align(Alignment.End)
                )
            }
        }
    }
}

@Composable
fun BookDetailsInHome(
    modifier: Modifier = Modifier,
    bookName: String = "",
    bookDesc: String = ""
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
        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = "Book Name",
                color = MyPurple80,
                fontSize = 18.sp,
                fontWeight = FontWeight.Normal,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = bookName,
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(MyPurple120)
                    .padding(horizontal = 12.dp)
                    .weight(2f)
                    .horizontalScroll(rememberScrollState()),
                maxLines = 1,
                overflow = TextOverflow.Clip
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = bookDesc,
            color = MyPurple80,
            fontSize = 14.sp,
            maxLines = 4,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun EmptyCardInHome(
    message: String = ""
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .border(1.dp, Color.Black, RoundedCornerShape(20.dp))
            .background(Color.White)
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = message,
            color = MyPurple80,
            fontSize = 18.sp,
            modifier = Modifier.padding(vertical = 32.dp)
        )
    }
}