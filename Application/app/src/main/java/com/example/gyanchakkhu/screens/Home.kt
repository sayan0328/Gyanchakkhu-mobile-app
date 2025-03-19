package com.example.gyanchakkhu.screens

import NoticeBoard
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.gyanchakkhu.R
import com.example.gyanchakkhu.ui.theme.Blue80
import com.example.gyanchakkhu.ui.theme.MyPurple120
import com.example.gyanchakkhu.ui.theme.MyPurple80
import com.example.gyanchakkhu.utils.BookDetailsInHome
import com.example.gyanchakkhu.utils.MyHorizontalDivider
import com.example.gyanchakkhu.utils.RecentIssuesInHome
import com.example.gyanchakkhu.utils.Routes
import com.example.gyanchakkhu.utils.gradientBrush
import com.example.gyanchakkhu.viewmodels.AuthState
import com.example.gyanchakkhu.viewmodels.AuthViewModel
import com.example.gyanchakkhu.viewmodels.BooksViewModel
import kotlin.Float.Companion.POSITIVE_INFINITY

@Composable
fun HomePage(navController: NavController, authViewModel: AuthViewModel, booksViewModel: BooksViewModel) {
    val authState = authViewModel.authState.observeAsState()
    val isUserEnrolledInLibrary by authViewModel.isEnrolledInLibrary.observeAsState(false)
    val userData by authViewModel.userData.observeAsState()
    val libName by authViewModel.libraryName.observeAsState()
    val userLibName = libName ?: "Not Found!"
    val cardUid = userData?.cardUid ?: "Not Found!"
    val myBooks by booksViewModel.myBooks.collectAsState()
    val recentlyIssuedBooks = myBooks.take(3)
    val libBooks by booksViewModel.books.collectAsState()
    val libraryBooks = libBooks.take(3)
    var showNoticeBoard by remember { mutableStateOf(false) }
    val gradient = gradientBrush(
        colorStops = arrayOf(
            0.0f to MyPurple120,
            2.0f to Color.White
        ),
        start = Offset(0f, 0f),
        end = Offset(0f, POSITIVE_INFINITY)
    )

    LaunchedEffect(authState.value) {
        when (authState.value) {
            is AuthState.Unauthenticated -> {
                navController.navigate(Routes.login_page)
            }

            else -> Unit
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(gradient)
        ) {
            if (!isUserEnrolledInLibrary) {
                Image(
                    painter = painterResource(id = R.drawable.bg_idle),
                    contentDescription = "Home Bg",
                    Modifier.align(Alignment.Center)
                )
            }
            LazyColumn(
                modifier = Modifier.fillMaxWidth()
            ) {
                item {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Spacer(modifier = Modifier.height(30.dp))
                        Image(
                            painter = painterResource(id = R.drawable.bg_home),
                            contentDescription = "Login/SignUp Bg",
                            modifier = Modifier.fillMaxWidth(),
                            contentScale = ContentScale.FillWidth
                        )
                        Spacer(modifier = Modifier.height(30.dp))
                        if (!isUserEnrolledInLibrary) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 20.dp)
                                    .clip(RoundedCornerShape(20.dp))
                                    .background(Color.White)
                                    .padding(horizontal = 20.dp)
                                    .height(36.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = stringResource(id = R.string.complete_profile),
                                    fontSize = 14.sp
                                )
                                Text(
                                    text = "Goto Profile",
                                    color = Blue80,
                                    fontSize = 14.sp,
                                    modifier = Modifier
                                        .clickable {
                                            navController.navigate(Routes.profile_page) {
                                                popUpTo(Routes.home_page) {
                                                    inclusive = true
                                                }
                                            }
                                        }
                                )
                            }
                            Spacer(modifier = Modifier.height(30.dp))
                        } else {
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 36.dp),
                                    horizontalArrangement = Arrangement.Center,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(
                                        modifier = Modifier.weight(1f),
                                        horizontalAlignment = Alignment.Start,
                                        verticalArrangement = Arrangement.Center
                                    ) {
                                        Text(
                                            text = userLibName,
                                            fontWeight = FontWeight.SemiBold,
                                            fontSize = 24.sp
                                        )
                                        Text(
                                            text = cardUid
                                        )
                                    }
                                    Image(
                                        painter = painterResource(R.drawable.home_notice),
                                        contentDescription = "Home Notice Board",
                                        Modifier.clickable { showNoticeBoard = true }
                                    )
                                }
                                MyHorizontalDivider()
                                Text(
                                    text = stringResource(R.string.home_recent_message),
                                    color = MyPurple80,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 20.sp
                                )
                                Column(
                                    modifier = Modifier.fillMaxWidth().padding(horizontal = 18.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    recentlyIssuedBooks.forEach {book ->
                                        Spacer(modifier = Modifier.height(16.dp))
                                        RecentIssuesInHome(
                                            bookName = book.bookName,
                                            bookId = book.bookId,
                                            issueDate = "12/02/24",
                                            submitDate = "15/03/24"
                                        )
                                    }
                                }
                                MyHorizontalDivider()
                                Text(
                                    text = stringResource(R.string.home_recommended_message),
                                    color = MyPurple80,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 20.sp
                                )
                                Column(
                                    modifier = Modifier.fillMaxWidth().padding(horizontal = 18.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    libraryBooks.forEach {book ->
                                        Spacer(modifier = Modifier.height(16.dp))
                                        BookDetailsInHome(
                                            bookName = book.bookName,
                                            bookDesc = "Blah Blah Blah"
                                        )
                                    }
                                }
                                MyHorizontalDivider()
                                Text(
                                    text = stringResource(R.string.home_trending_message),
                                    color = MyPurple80,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 20.sp
                                )
                                Column(
                                    modifier = Modifier.fillMaxWidth().padding(horizontal = 18.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    libraryBooks.forEach {book ->
                                        Spacer(modifier = Modifier.height(16.dp))
                                        BookDetailsInHome(
                                            bookName = book.bookName,
                                            bookDesc = "Blah Blah Blah"
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.height(160.dp))
                            }
                        }
                    }
                }
            }
            if(showNoticeBoard){
                NoticeBoard { showNoticeBoard = false }
            }
        }
    }
}