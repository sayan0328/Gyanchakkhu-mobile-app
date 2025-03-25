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
import androidx.navigation.NavController
import com.example.gyanchakkhu.R
import com.example.gyanchakkhu.ui.theme.Blue80
import com.example.gyanchakkhu.ui.theme.MyPurple120
import com.example.gyanchakkhu.ui.theme.Purple40
import com.example.gyanchakkhu.ui.theme.poppinsFontFamily
import com.example.gyanchakkhu.utils.BookDetailsInHistory
import com.example.gyanchakkhu.utils.CustomSearchBar
import com.example.gyanchakkhu.utils.Routes
import com.example.gyanchakkhu.utils.fadingEdge
import com.example.gyanchakkhu.utils.gradientBrush
import com.example.gyanchakkhu.viewmodels.AuthState
import com.example.gyanchakkhu.viewmodels.AuthViewModel
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
            if (!isUserEnrolledInLibrary) {
                Image(
                    painter = painterResource(id = R.drawable.bg_idle),
                    contentDescription = "Home Bg",
                    Modifier.align(Alignment.Center)
                )
            }
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
                            fontSize = 14.sp,
                            style = TextStyle(
                                fontFamily = poppinsFontFamily
                            )
                        )
                        Text(
                            text = "Goto Profile",
                            color = Blue80,
                            fontSize = 14.sp,
                            style = TextStyle(
                                fontFamily = poppinsFontFamily
                            ),
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
                                        isPending = submitDate.before(today.time)
                                    )
                                }
                            }
                            issuedBooks.forEach { (submitDate,  books) ->
                                items(books) { book ->
                                    Spacer(modifier = Modifier.height(16.dp))
                                    BookDetailsInHistory(
                                        book = book,
                                        isPending = submitDate.before(today.time)
                                    )
                                }
                            }
                            submittedBooks.forEach { (submitDate,  books) ->
                                items(books) { book ->
                                    Spacer(modifier = Modifier.height(16.dp))
                                    BookDetailsInHistory(
                                        book = book,
                                        isPending = submitDate.before(today.time)
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
    }
}

fun parseDate(dateFormat: SimpleDateFormat, dateStr: String): Date {
    return try{
        dateFormat.parse(dateStr) ?: Date()
    } catch (e: Exception) {
        Date()
    }
}