package com.example.gyanchakkhu.screens

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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import com.example.gyanchakkhu.R
import com.example.gyanchakkhu.ui.theme.Blue80
import com.example.gyanchakkhu.ui.theme.MyPurple120
import com.example.gyanchakkhu.ui.theme.Purple40
import com.example.gyanchakkhu.ui.theme.poppinsFontFamily
import com.example.gyanchakkhu.utils.BookDetailsInHistory
import com.example.gyanchakkhu.utils.CustomSearchBar
import com.example.gyanchakkhu.utils.ExpandedBookDetailsInSearch
import com.example.gyanchakkhu.utils.GoToProfile
import com.example.gyanchakkhu.utils.Routes
import com.example.gyanchakkhu.utils.fadingEdge
import com.example.gyanchakkhu.utils.gradientBrush
import com.example.gyanchakkhu.viewmodels.AuthState
import com.example.gyanchakkhu.viewmodels.AuthViewModel
import com.example.gyanchakkhu.viewmodels.Book
import com.example.gyanchakkhu.viewmodels.BooksViewModel
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale
import kotlin.Float.Companion.POSITIVE_INFINITY

@Composable
fun HistoryPage(
    navController: NavController,
    authViewModel: AuthViewModel,
    booksViewModel: BooksViewModel
) {
    val isUserEnrolledInLibrary by authViewModel.isEnrolledInLibrary.observeAsState(false)
    val dateFormat =SimpleDateFormat("dd/MM/yy", Locale.getDefault())
    val today = Calendar.getInstance()
    val myBooks by booksViewModel.myBooks.collectAsState()
    val libBooks by booksViewModel.books.collectAsState()
    val pendingBooks = myBooks.filter { !it.isSubmitted && parseDate(dateFormat, it.submitDate).before(today.time) }.groupBy { book ->
        parseDate(dateFormat, book.submitDate)
    }.toSortedMap(compareByDescending( {it.time} ))
    val issuedBooks = myBooks.filter { !it.isSubmitted && parseDate(dateFormat, it.submitDate).after(today.time) }.groupBy { book ->
        parseDate(dateFormat, book.submitDate)
    }.toSortedMap(compareByDescending( {it.time} ))
    val submittedBooks = myBooks.filter { it.isSubmitted }.groupBy { book ->
        parseDate(dateFormat, book.submitDate)
    }.toSortedMap(compareByDescending( {it.time} ))
    val searchText by booksViewModel.searchText.collectAsState()
    val isSearching by booksViewModel.isSearching.collectAsState()
    var expandedBook by remember { mutableStateOf(Book("", "", "", "", "")) }
    var expandedIssuedBook by remember { mutableStateOf(Book("", "", "", "", "")) }
    var showExpandedDetails by remember { mutableStateOf(false) }
    var showExpandedIssuedDetails by remember { mutableStateOf(false) }

    val topFade = Brush.verticalGradient(0f to Color.Transparent, 0.05f to Color.Black)
    val gradient = gradientBrush(
        colorStops = arrayOf(
            0.0f to MyPurple120,
            2.0f to Color.White
        ),
        start = Offset(0f, 0f),
        end = Offset(0f, POSITIVE_INFINITY)
    )

    val authState = authViewModel.authState.observeAsState()
    LaunchedEffect(authState.value) {
        when (authState.value) {
            is AuthState.Unauthenticated -> navController.navigate(Routes.login_page)
            is AuthState.Error -> {
                navController.navigate(Routes.login_page)
            }
            else -> Unit
        }
        booksViewModel.clearSearchText()
    }

    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(gradient)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.bg_home),
                    contentDescription = "Login/SignUp Bg",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(30.dp),
                    contentScale = ContentScale.FillWidth
                )
                if (!isUserEnrolledInLibrary) {
                    GoToProfile {
                        navController.navigate(Routes.profile_page) {
                            popUpTo(Routes.home_page) {
                                inclusive = true
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                }
                CustomSearchBar(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    value = searchText,
                    onValueChange = booksViewModel::onSearchTextChange,
                    placeholderText = "Search your book",
                    containerColor = Color.White
                )
                if (isSearching) {
                    Box(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fadingEdge(topFade)
                            .fillMaxWidth()
                            .weight(1f)
                            .padding(horizontal = 16.dp)
                    ) {
                        if (myBooks.isEmpty()) {
                            item {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Image(
                                        painter = painterResource(R.drawable.empty_booklist),
                                        contentDescription = "History is Empty",
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(top = 40.dp, bottom = 16.dp)
                                    )
                                    Text(
                                        text = stringResource(R.string.history_page_message),
                                        color = Purple40,
                                        style = TextStyle(
                                            fontFamily = poppinsFontFamily
                                        ),
                                        fontWeight = FontWeight.SemiBold,
                                    )
                                }
                            }

                        } else {
                            item {
                                Spacer(modifier = Modifier.height(12.dp))
                            }
                            pendingBooks.forEach { (submitDate,  books) ->
                                items(books) { book ->
                                    Spacer(modifier = Modifier.height(16.dp))
                                    BookDetailsInHistory(
                                        book = book,
                                        onClick = {
                                            expandedIssuedBook = libBooks.find { it.bookId == book.bookId }!!
                                            showExpandedIssuedDetails = true
                                        },
                                        isPending = submitDate.before(today.time)
                                    )
                                }
                            }
                            issuedBooks.forEach { (submitDate,  books) ->
                                items(books) { book ->
                                    Spacer(modifier = Modifier.height(16.dp))
                                    BookDetailsInHistory(
                                        book = book,
                                        onClick = {
                                            expandedIssuedBook = libBooks.find { it.bookId == book.bookId }!!
                                            showExpandedIssuedDetails = true
                                        },
                                        isPending = submitDate.before(today.time)
                                    )
                                }
                            }
                            submittedBooks.forEach { (submitDate,  books) ->
                                items(books) { book ->
                                    Spacer(modifier = Modifier.height(16.dp))
                                    BookDetailsInHistory(
                                        book = book,
                                        onClick = {
                                            expandedBook = libBooks.find { it.bookId == book.bookId }!!
                                            showExpandedDetails = true
                                        },
                                        isPending = submitDate.before(today.time),

                                    )
                                }
                            }
                        }
                        item {
                            Spacer(modifier = Modifier.height(160.dp))
                        }
                    }
                }
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

fun parseDate(dateFormat: SimpleDateFormat, dateStr: String): Date {
    return try{
        dateFormat.parse(dateStr) ?: Date()
    } catch (e: Exception) {
        Date()
    }
}