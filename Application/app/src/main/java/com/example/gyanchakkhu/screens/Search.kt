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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.gyanchakkhu.R
import com.example.gyanchakkhu.ui.theme.Blue80
import com.example.gyanchakkhu.ui.theme.MyPurple120
import com.example.gyanchakkhu.ui.theme.Purple40
import com.example.gyanchakkhu.utils.BookDetailsInSearch
import com.example.gyanchakkhu.utils.CustomSearchBar
import com.example.gyanchakkhu.utils.Routes
import com.example.gyanchakkhu.utils.gradientBrush
import com.example.gyanchakkhu.viewmodels.AuthState
import com.example.gyanchakkhu.viewmodels.AuthViewModel
import com.example.gyanchakkhu.viewmodels.BooksViewModel
import kotlin.Float.Companion.POSITIVE_INFINITY

@Composable
fun SearchPage(navController: NavController, authViewModel: AuthViewModel) {
    val booksViewModel = viewModel<BooksViewModel>()
    val isUserEnrolledInLibrary by authViewModel.isEnrolledInLibrary.observeAsState(false)
    val searchText by booksViewModel.searchText.collectAsState()
    val books by booksViewModel.books.collectAsState()
    val isSearching by booksViewModel.isSearching.collectAsState()
    val isSearchBarEmpty by booksViewModel.isSearchBarEmpty.collectAsState()

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
                    Spacer(modifier = Modifier.height(24.dp))
                }
                CustomSearchBar(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    value = searchText,
                    onValueChange = booksViewModel::onSearchTextChange,
                    placeholderText = "Bangla Choti Golpo",
                    containerColor = Color.White
                )

//                Spacer(modifier = Modifier.height(16.dp))
//                HorizontalDivider(
//                    modifier = Modifier.padding(horizontal = 24.dp),
//                    color = Color.Black
//                )

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
                            .fillMaxWidth()
                            .weight(1f)
                            .padding(horizontal = 24.dp)
                    ) {
                        if (isSearchBarEmpty) {
                            item {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Image(
                                        painter = painterResource(R.drawable.empty_booklist),
                                        contentDescription = "Search Bar is Empty",
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(top = 40.dp, bottom = 16.dp)
                                    )
                                    Text(
                                        text = stringResource(R.string.search_page_message),
                                        color = Purple40,
                                        fontWeight = FontWeight.SemiBold,
                                    )
                                }
                            }

                        } else {
                            items(books) { book ->
                                Spacer(modifier = Modifier.height(24.dp))
                                BookDetailsInSearch(
                                    bookName = book.bookName,
                                    bookId = book.bookId,
                                    librarySection = book.libSection,
                                    rackNo = book.rackNo
                                )
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