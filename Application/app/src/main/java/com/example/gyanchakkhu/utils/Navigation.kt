package com.example.gyanchakkhu.utils

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.gyanchakkhu.screens.BooksPage
import com.example.gyanchakkhu.screens.HistoryPage
import com.example.gyanchakkhu.screens.HomePage
import com.example.gyanchakkhu.screens.LoginPage
import com.example.gyanchakkhu.screens.ProfilePage
import com.example.gyanchakkhu.screens.SearchPage
import com.example.gyanchakkhu.screens.SignupPage
import com.example.gyanchakkhu.ui.theme.Blue40
import com.example.gyanchakkhu.viewmodels.AuthState
import com.example.gyanchakkhu.viewmodels.AuthViewModel
import com.example.gyanchakkhu.viewmodels.BooksViewModel

@Composable
fun AppNavigation(authViewModel: AuthViewModel, booksViewModel: BooksViewModel) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val books by booksViewModel.books.collectAsState()
    Scaffold(
        floatingActionButton = {
            if (navBackStackEntry?.destination?.route in listOfNavItems.map { it.route }) {
                Box(modifier = Modifier.offset(y = 16.dp)) {
                    CustomNavigationBar(
                        currentDestination = currentDestination,
                        items = listOfNavItems,
                        onItemClick = { item ->
                            navController.navigate(item.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        },
        floatingActionButtonPosition = FabPosition.Center
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Routes.home_page,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Routes.login_page) {
                LoginPage(navController, authViewModel)
            }
            composable(Routes.signup_page) {
                SignupPage(navController, authViewModel)
            }
            composable(Routes.home_page) {
                HomePage(navController, authViewModel, booksViewModel)
            }
            composable(Routes.books_page) {
                BooksPage(navController, authViewModel, booksViewModel)
            }
//            composable(Routes.issue_page) {
//                IssuePage()
//            }
//            composable(Routes.submit_page) {
//                SubmitPage()
//            }
            composable(Routes.search_page) {
                SearchPage(navController, authViewModel, booksViewModel)
            }
            composable(Routes.history_page) {
                HistoryPage(navController, authViewModel, booksViewModel)
            }
            composable(Routes.profile_page) {
                ProfilePage(navController, authViewModel)
            }
        }
    }
}
