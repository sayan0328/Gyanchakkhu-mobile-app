package com.example.gyanchakkhu.screens

import NoticeBoard
import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import com.example.gyanchakkhu.R
import com.example.gyanchakkhu.ui.theme.Blue40
import com.example.gyanchakkhu.ui.theme.MyPurple100
import com.example.gyanchakkhu.ui.theme.MyPurple120
import com.example.gyanchakkhu.ui.theme.poppinsFontFamily
import com.example.gyanchakkhu.utils.BookDetailsInHistory
import com.example.gyanchakkhu.utils.BookDetailsInSearch
import com.example.gyanchakkhu.utils.ExpandedBookDetailsInSearch
import com.example.gyanchakkhu.utils.GoToProfile
import com.example.gyanchakkhu.utils.Routes
import com.example.gyanchakkhu.utils.gradientBrush
import com.example.gyanchakkhu.viewmodels.AuthState
import com.example.gyanchakkhu.viewmodels.AuthViewModel
import com.example.gyanchakkhu.viewmodels.Book
import com.example.gyanchakkhu.viewmodels.BooksViewModel
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.Float.Companion.POSITIVE_INFINITY

@Composable
fun HomePage(
    navController: NavController,
    authViewModel: AuthViewModel,
    booksViewModel: BooksViewModel
) {
    val context = LocalContext.current
    val authState = authViewModel.authState.observeAsState()
    val dateFormat = SimpleDateFormat("dd/MM/yy", Locale.getDefault())
    val isUserEnrolledInLibrary by authViewModel.isEnrolledInLibrary.observeAsState(false)
    val userData by authViewModel.userData.observeAsState()
    val libName by authViewModel.libraryName.observeAsState()
    val userLibName = libName ?: "Not Found!"
    val cardUid = userData?.cardUid ?: "Not Found!"
    val myBooks by booksViewModel.myBooks.collectAsState()
    val recentlyIssuedBooks = myBooks
        .filter { !it.isSubmitted }
        .sortedByDescending { parseDate(dateFormat, it.submitDate).time }
        .take(3)
    val libBooks by booksViewModel.books.collectAsState()
    val recommendedBooks = libBooks.asSequence().shuffled().take(6).toList()
    val trendingBooks = libBooks.asSequence().shuffled().take(6).toList()
    var showNoticeBoard by remember { mutableStateOf(false) }
    var expandedBook by remember { mutableStateOf(Book("", "", "", "", "")) }
    var expandedIssuedBook by remember { mutableStateOf(Book("", "", "", "", "")) }
    var showExpandedDetails by remember { mutableStateOf(false) }
    var showExpandedIssuedDetails by remember { mutableStateOf(false) }
    val showPing by authViewModel.showPing.collectAsState()
    val gradient = gradientBrush(
        colorStops = arrayOf(
            0.0f to MyPurple120,
            2.0f to Color.White
        ),
        start = Offset(0f, 0f),
        end = Offset(0f, POSITIVE_INFINITY)
    )

    BackHandler {
        (context as? Activity)?.finish()
    }

    LaunchedEffect(authState.value) {
        when (authState.value) {
            is AuthState.Unauthenticated -> {
                navController.navigate(Routes.login_page)
            }
            is AuthState.Error -> {
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
                modifier = Modifier.fillMaxSize()
            ) {
                item {
                    Image(
                        painter = painterResource(id = R.drawable.bg_home),
                        contentDescription = "Login/SignUp Bg",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(30.dp),
                        contentScale = ContentScale.FillWidth
                    )
                }
                item {
                    if (!isUserEnrolledInLibrary) {
                        GoToProfile {
                            navController.navigate(Routes.profile_page) {
                                popUpTo(Routes.home_page) {
                                    inclusive = true
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(24.dp))
                    } else {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp),
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
                                        fontFamily = poppinsFontFamily,
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 24.sp,
                                        modifier = Modifier.horizontalScroll(rememberScrollState())
                                    )
                                    Text(
                                        text = cardUid,
                                        fontFamily = poppinsFontFamily
                                    )
                                }
                                Image(
                                    painter = painterResource(if (showPing) R.drawable.home_notice_ping else R.drawable.home_notice),
                                    contentDescription = "Home Notice Board",
                                    Modifier
                                        .size(36.dp)
                                        .clickable {
                                            showNoticeBoard = true
                                            authViewModel.setPingOff()
                                        }
                                )
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                            if (myBooks.isEmpty()) {
                                Image(
                                    painter = painterResource(R.drawable.empty_mind),
                                    contentDescription = "No books found",
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .size(200.dp)
                                )
                                Text(
                                    text = stringResource(R.string.home_empty_message),
                                    color = MyPurple100,
                                    fontFamily = poppinsFontFamily,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 24.sp,
                                    modifier = Modifier
                                        .padding(horizontal = 16.dp)
                                        .align(Alignment.Start)
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                LazyRow {
                                    item { Spacer(modifier = Modifier.width(16.dp)) }
                                    items(trendingBooks) { book ->
                                        BookDetailsInSearch(
                                            book = book,
                                            onClick = {
                                                expandedBook = book
                                                showExpandedDetails = true
                                            }
                                        )
                                        Spacer(modifier = Modifier.width(16.dp))
                                    }
                                }
                                Spacer(modifier = Modifier.height(16.dp))
                                LazyRow {
                                    item { Spacer(modifier = Modifier.width(16.dp)) }
                                    items(recommendedBooks) { book ->
                                        BookDetailsInSearch(
                                            book = book,
                                            onClick = {
                                                expandedBook = book
                                                showExpandedDetails = true
                                            }
                                        )
                                        Spacer(modifier = Modifier.width(16.dp))
                                    }
                                }
                                Spacer(modifier = Modifier.height(20.dp))
                                Text(
                                    text = buildAnnotatedString {
                                        withStyle(style = SpanStyle(color = MyPurple100)) {
                                            append("For more, go to ")
                                        }
                                        append("Search")
                                    },
                                    color = Blue40,
                                    fontFamily = poppinsFontFamily,
                                    fontSize = 20.sp,
                                    modifier = Modifier
                                        .clickable {
                                            navController.popBackStack()
                                            navController.navigate(Routes.search_page)
                                        }
                                )
                            } else {
                                if (recentlyIssuedBooks.isEmpty()) {
                                    Image(
                                        painter = painterResource(R.drawable.empty_mind),
                                        contentDescription = "No books found",
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .size(200.dp)
                                    )
                                } else {
                                    Text(
                                        text = stringResource(R.string.home_recent_message),
                                        color = MyPurple100,
                                        fontFamily = poppinsFontFamily,
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 24.sp,
                                        modifier = Modifier
                                            .padding(horizontal = 16.dp)
                                            .align(Alignment.Start)
                                    )
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 16.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.Center
                                    ) {
                                        recentlyIssuedBooks.forEach { book ->
                                            Spacer(modifier = Modifier.height(16.dp))
                                            BookDetailsInHistory(
                                                book = book,
                                                onClick = {
                                                    expandedIssuedBook = libBooks.find { it.bookId == book.bookId }!!
                                                    showExpandedIssuedDetails = true
                                                }
                                            )
                                        }
                                    }
                                }
                                Spacer(modifier = Modifier.height(20.dp))
                                Text(
                                    text = stringResource(R.string.home_recommended_message),
                                    color = MyPurple100,
                                    fontFamily = poppinsFontFamily,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 24.sp,
                                    modifier = Modifier
                                        .padding(horizontal = 16.dp)
                                        .align(Alignment.Start)
                                )
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Spacer(modifier = Modifier.height(16.dp))
                                    LazyRow {
                                        item { Spacer(modifier = Modifier.width(16.dp)) }
                                        items(recommendedBooks) { book ->
                                            BookDetailsInSearch(
                                                book = book,
                                                onClick = {
                                                    expandedBook = book
                                                    showExpandedDetails = true
                                                }
                                            )
                                            Spacer(modifier = Modifier.width(16.dp))
                                        }
                                    }
                                }
                                Spacer(modifier = Modifier.height(20.dp))
                                Text(
                                    text = stringResource(R.string.home_trending_message),
                                    color = MyPurple100,
                                    fontFamily = poppinsFontFamily,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 24.sp,
                                    modifier = Modifier
                                        .padding(horizontal = 16.dp)
                                        .align(Alignment.Start)
                                )
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Spacer(modifier = Modifier.height(16.dp))
                                    LazyRow {
                                        item { Spacer(modifier = Modifier.width(16.dp)) }
                                        items(trendingBooks) { book ->
                                            BookDetailsInSearch(
                                                book = book,
                                                onClick = {
                                                    expandedBook = book
                                                    showExpandedDetails = true
                                                }
                                            )
                                            Spacer(modifier = Modifier.width(16.dp))
                                        }
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(140.dp))
                        }
                    }
                }
            }
        }
        if (showNoticeBoard) {
            Dialog(
                onDismissRequest = { showNoticeBoard = false },
                properties = DialogProperties(usePlatformDefaultWidth = false)
            ) {
                NoticeBoard { showNoticeBoard = false }
            }
        }
        if (showExpandedDetails) {
            Dialog(
                onDismissRequest = { showExpandedDetails = false },
                properties = DialogProperties(usePlatformDefaultWidth = false)
            ) {
                ExpandedBookDetailsInSearch(
                    book = expandedBook,
                    onClose = {
                        showExpandedDetails = false
                        expandedBook = Book("", "", "", "", "")
                    },
                    onAction = {
                        showExpandedDetails = false
                        navController.popBackStack()
                        navController.navigate(Routes.books_page)
                    },
                    showSimilar = {
                        showExpandedDetails = false
                        navController.popBackStack()
                        navController.navigate(Routes.search_page)
                    },
                    message = "Go To Search",
                    actionMessage = "Issue Now"
                )
            }
        }
        if (showExpandedIssuedDetails) {
            Dialog(
                onDismissRequest = { showExpandedIssuedDetails = false },
                properties = DialogProperties(usePlatformDefaultWidth = false)
            ) {
                ExpandedBookDetailsInSearch(
                    book = expandedIssuedBook,
                    onClose = {
                        showExpandedIssuedDetails = false
                        expandedIssuedBook = Book("", "", "", "", "")
                    },
                    onAction = {
                        showExpandedIssuedDetails = false
                        navController.popBackStack()
                        navController.navigate(Routes.books_page)
                    },
                    showSimilar = {
                        showExpandedIssuedDetails = false
                        navController.popBackStack()
                        navController.navigate(Routes.search_page)
                    },
                    message = "Go To Search",
                    actionMessage = "Submit Now"
                )
            }
        }
    }
}
