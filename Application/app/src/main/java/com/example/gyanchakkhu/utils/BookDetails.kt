package com.example.gyanchakkhu.utils

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gyanchakkhu.R
import com.example.gyanchakkhu.ui.theme.Blue40
import com.example.gyanchakkhu.ui.theme.Green80
import com.example.gyanchakkhu.ui.theme.MyPurple100
import com.example.gyanchakkhu.ui.theme.MyPurple120
import com.example.gyanchakkhu.ui.theme.MyPurple40
import com.example.gyanchakkhu.ui.theme.MyPurple60
import com.example.gyanchakkhu.ui.theme.poppinsFontFamily
import com.example.gyanchakkhu.viewmodels.Book
import com.example.gyanchakkhu.viewmodels.MyBook

@Composable
fun BookDetailsInIssueAndSubmit(
    modifier: Modifier = Modifier,
    actionLabel: String,
    book: Book,
    onClick: () -> Unit,
    onClose: () -> Unit
) {
    val bitmap = remember(book.coverImage) { decodeBase64ToBitmap(book.coverImage) }
    val bookDetails = listOf(
        "Publication Year" to book.publicationYear,
        "Edition" to book.edition,
        "Publisher" to book.publisher,
        "Genre" to book.genre,
    )
    Column(
        modifier = modifier
            .padding(16.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
            Column(
                modifier = modifier
                    .padding(16.dp)
                    .background(Color.White),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = book.bookName,
                    color = MyPurple40,
                    fontSize = 28.sp,
                    fontFamily = poppinsFontFamily,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.Start).horizontalScroll(rememberScrollState())
                )
                Text(
                    text = "by ${book.author}",
                    color = MyPurple40,
                    fontSize = 20.sp,
                    fontFamily = poppinsFontFamily,
                    modifier = Modifier.align(Alignment.Start).horizontalScroll(rememberScrollState()).offset(y = (-4).dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier,
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.Top
                ) {
                    Column(
                    ) {
                        Box(
                            modifier = Modifier
                                .height(172.dp)
                                .aspectRatio(2f/3f)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color.White)
                        ) {
                            bitmap?.let {
                                Image(
                                    bitmap = it.asImageBitmap(),
                                    contentDescription = "Book Cover",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                            } ?: Image(
                                painter = painterResource(R.drawable.sample_cover),
                                contentDescription = "Book Cover",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        }

                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(
                    ) {

                        bookDetails.forEach  { (label, value) ->
                            Text(
                                text = buildAnnotatedString {
                                    withStyle(style = SpanStyle(fontWeight = FontWeight.SemiBold)) {
                                        append("$label: ")
                                    }
                                    append(value)
                                },
                                color = MyPurple100,
                                fontFamily = poppinsFontFamily,
                                fontSize = 18.sp
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column (
                        modifier = Modifier
                            .height(28.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .background(Color.Gray)
                            .clickable {
                                onClose()
                            },
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Close",
                            color = Color.White,
                            fontFamily = poppinsFontFamily,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(horizontal = 24.dp)
                        )
                    }
                    Column (
                        modifier = Modifier
                            .height(28.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .background(Blue40)
                            .clickable {
                                if (book.bookId.isNotEmpty()) onClick() else Unit
                            },
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = actionLabel,
                            color = Color.White,
                            fontFamily = poppinsFontFamily,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(horizontal = 24.dp)
                        )
                    }
                }
            }

    }
}

@Composable
fun BookDetailsInSearch(
    book: Book,
    onClick: () -> Unit
) {
    val bitmap = remember(book.coverImage) { decodeBase64ToBitmap(book.coverImage) }
    Card(
        modifier = Modifier
            .width(120.dp)
            .clickable {
                onClick()
            },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.background(MyPurple120)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
            ) {
                bitmap?.let {
                    Image(
                        bitmap = it.asImageBitmap(),
                        contentDescription = "Book Cover",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } ?: Image(
                    painter = painterResource(R.drawable.sample_cover),
                    contentDescription = "Book Cover",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                Image(
                    painter = painterResource(R.drawable.navigate_arrow),
                    contentDescription = "Navigate Arrow",
                    modifier = Modifier
                        .size(28.dp)
                        .align(Alignment.BottomEnd)
                        .padding(4.dp)
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = book.bookName,
                    color = MyPurple40,
                    fontFamily = poppinsFontFamily,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    modifier = Modifier.horizontalScroll(rememberScrollState())
                )
            }
        }
    }
}

@Composable
fun ExpandedBookDetailsInSearch(
    book: Book,
    onClose: () -> Unit,
    onAction: () -> Unit,
    showSimilar: () -> Unit,
    message: String = "",
    actionMessage: String = ""
) {
    val bitmap = remember(book.coverImage) { decodeBase64ToBitmap(book.coverImage) }
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Card(
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .padding(16.dp)
                .wrapContentSize(),
            colors = CardDefaults.cardColors(Color.White)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Top
                ) {
                    Column(
                        modifier = Modifier.weight(6.5f)
                    ) {
                        Box(
                            modifier = Modifier
                                .height(256.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color.White)
                        ) {
                            bitmap?.let {
                                Image(
                                    bitmap = it.asImageBitmap(),
                                    contentDescription = "Book Cover",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                            } ?: Image(
                                painter = painterResource(R.drawable.sample_cover),
                                contentDescription = "Book Cover",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                        Text(
                            text = book.bookName,
                            color = MyPurple40,
                            fontSize = 28.sp,
                            fontFamily = poppinsFontFamily,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.horizontalScroll(rememberScrollState())
                        )
                        Text(
                            text = "by ${book.author}",
                            color = MyPurple40,
                            fontSize = 16.sp,
                            fontFamily = poppinsFontFamily,
                            modifier = Modifier.horizontalScroll(rememberScrollState()).offset(y = (-4).dp)
                        )
                    }
                    Spacer(modifier = Modifier.weight(0.5f))
                    Column(
                        modifier = Modifier
                            .weight(5.5f)
                    ) {
                        Text(
                            text = "BOOK DETAILS",
                            color = MyPurple100,
                            fontFamily = poppinsFontFamily,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 17.sp,
                        )
                        val details = listOf(
                            "Publication Year" to book.publicationYear,
                            "Edition" to book.edition,
                            "Language" to book.language,
                            "Publisher" to book.publisher,
                            "Genre" to book.genre,
                            "Page Count" to book.pageCount,
                            "ISBN No." to book.isbnNo,
                            "Status" to "Available"
                        )
                        details.forEach  { (label, value) ->
                            Text(
                                text = buildAnnotatedString {
                                    withStyle(style = SpanStyle(fontWeight = FontWeight.SemiBold)) {
                                        append("$label: ")
                                    }
                                    append(value)
                                },
                                color = MyPurple100,
                                fontFamily = poppinsFontFamily,
                                fontSize = 12.sp
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    listOf(
                        "Rack: " to book.rackNo,
                        "Section: " to book.librarySection
                    ).forEach { (label, value) ->
                        Column (
                            modifier = Modifier
                                .height(28.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .background(MyPurple120),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "$label$value",
                                color = MyPurple60,
                                fontFamily = poppinsFontFamily,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(horizontal = 9.dp)
                            )
                        }
                        if(label == "Rack: "){
                            Spacer(modifier = Modifier.width(8.dp))
                        }
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Column (
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(28.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .background(Blue40)
                            .clickable {
                                onAction()
                            },
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = actionMessage,
                            color = Color.White,
                            fontFamily = poppinsFontFamily,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                        )
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
                LazyColumn(
                    modifier = Modifier.height(108.dp)
                ) {
                    item {
                        Text(
                            text = book.description,
                            color = MyPurple100,
                            fontFamily = poppinsFontFamily,
                            fontSize = 12.sp,
                            lineHeight = 18.sp
                        )
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column (
                        modifier = Modifier
                            .height(28.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .background(Color.Gray)
                            .clickable {
                                onClose()
                            },
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Close",
                            color = Color.White,
                            fontFamily = poppinsFontFamily,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(horizontal = 24.dp)
                        )
                    }
                    Column (
                        modifier = Modifier
                            .height(28.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .background(Blue40)
                            .clickable {
                                showSimilar()
                            },
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = message,
                            color = Color.White,
                            fontFamily = poppinsFontFamily,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(horizontal = 24.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun BookDetailsInHistory(
    book: MyBook,
    onClick: () -> Unit,
    isPending: Boolean = false
) {
    val bitmap = remember(book.coverImage) { decodeBase64ToBitmap(book.coverImage) }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .wrapContentHeight()
            ) {
                Text(
                    text = book.bookName,
                    color = MyPurple40,
                    fontSize = 20.sp,
                    style = TextStyle(
                        fontFamily = poppinsFontFamily
                    ),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.horizontalScroll(rememberScrollState())
                )
                Text(
                    text = "by ${book.author}",
                    color = MyPurple40,
                    style = TextStyle(
                        fontFamily = poppinsFontFamily
                    )
                )
                Spacer(modifier = Modifier.height(32.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "Issue Date",
                            color = MyPurple40,
                            fontFamily = poppinsFontFamily,
                            fontSize = 14.sp,
                            lineHeight = 14.sp
                        )
                        Text(
                            text = book.issueDate,
                            color = MyPurple40,
                            fontFamily = poppinsFontFamily,
                            fontSize = 12.sp
                        )
                    }
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "Submit Date",
                            color = MyPurple40,
                            fontFamily = poppinsFontFamily,
                            fontSize = 14.sp,
                            lineHeight = 14.sp,
                            modifier = Modifier.align(Alignment.End)
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if(book.isSubmitted) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = "Already Submitted",
                                    tint = Green80,
                                    modifier = Modifier.size(20.dp).align(Alignment.CenterVertically)
                                )
                            }else if(isPending) {
                                Icon(
                                    imageVector = Icons.Default.Info,
                                    contentDescription = "Submission Pending",
                                    tint = Green80,
                                    modifier = Modifier.rotate(90F).align(Alignment.CenterVertically)
                                )
                            }
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = book.submitDate,
                                color = MyPurple40,
                                fontFamily = poppinsFontFamily,
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.width(8.dp))
            Box(
                modifier = Modifier
                    .height(120.dp)
                    .aspectRatio(2f / 3f)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.White)
            ) {
                bitmap?.let {
                    Image(
                        bitmap = it.asImageBitmap(),
                        contentDescription = "Book Cover",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } ?: Image(
                    painter = painterResource(R.drawable.sample_cover),
                    contentDescription = "Book Cover",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}