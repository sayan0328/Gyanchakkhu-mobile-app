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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import com.example.gyanchakkhu.R
import com.example.gyanchakkhu.ui.theme.Blue80
import com.example.gyanchakkhu.ui.theme.MyPurple120
import com.example.gyanchakkhu.ui.theme.MyPurple80
import com.example.gyanchakkhu.ui.theme.Purple40
import com.example.gyanchakkhu.ui.theme.poppinsFontFamily
import com.example.gyanchakkhu.utils.BookDetailsInSearch
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
import kotlin.Float.Companion.POSITIVE_INFINITY

@Composable
fun SearchPage(
    navController: NavController,
    authViewModel: AuthViewModel,
    booksViewModel: BooksViewModel
) {
    val isUserEnrolledInLibrary by authViewModel.isEnrolledInLibrary.observeAsState(false)
    val searchText by booksViewModel.searchText.collectAsState()
    val books by booksViewModel.books.collectAsState()
    val groupedBooks  = books.groupBy { it.genre }.toSortedMap()
    val isSearching by booksViewModel.isSearching.collectAsState()
    var expandedBook by remember { mutableStateOf(Book("", "", "", "", "")) }
    var showExpandedDetails by remember { mutableStateOf(false) }
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
                    placeholderText = "Explore books by title, author, genre",
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
                    ) {
                        if (books.isEmpty()) {
                            item {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 16.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Image(
                                        painter = painterResource(R.drawable.empty_booklist),
                                        contentDescription = "No books found",
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(top = 40.dp, bottom = 16.dp)
                                    )
                                    Text(
                                        text = "No books found",
                                        color = Purple40,
                                        fontFamily = poppinsFontFamily,
                                        fontWeight = FontWeight.SemiBold,
                                    )
                                }
                            }
                        } else {
                            item {
                                Spacer(modifier = Modifier.height(24.dp))
                                groupedBooks.forEach { (genre, booksInGenre) ->
                                    Text(
                                        text = genre,
                                        color = MyPurple80,
                                        fontFamily = poppinsFontFamily,
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 24.sp,
                                        modifier = Modifier.padding(horizontal = 16.dp)
                                    )
                                    Spacer(modifier = Modifier.height(12.dp))
                                    LazyRow {
                                        item { Spacer(modifier = Modifier.width(16.dp)) }

                                        items(booksInGenre.sortedBy { it.bookName }) { book ->
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
                                    Spacer(modifier = Modifier.height(24.dp))
                                }
                            }

                        }

                        item {
                            Spacer(modifier = Modifier.height(140.dp))
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
                    },
                    toIssue = {
                        showExpandedDetails = false
                        navController.popBackStack()
                        navController.navigate(Routes.books_page)
                    },
                    showSimilar = {
                        showExpandedDetails = false
                        booksViewModel.onSearchTextChange(expandedBook.genre)
                    },
                    message = "Similar Books"
                )
            }
        }
    }
}