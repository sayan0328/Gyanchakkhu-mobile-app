package com.example.gyanchakkhu.utils

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.gyanchakkhu.screens.BooksPage
import com.example.gyanchakkhu.screens.HomePage
import com.example.gyanchakkhu.screens.IssuePage
import com.example.gyanchakkhu.screens.LoginPage
import com.example.gyanchakkhu.screens.ProfilePage
import com.example.gyanchakkhu.screens.RecordsPage
import com.example.gyanchakkhu.screens.SearchPage
import com.example.gyanchakkhu.screens.SignupPage
import com.example.gyanchakkhu.screens.SubmitPage
import com.example.gyanchakkhu.viewmodels.AuthState
import com.example.gyanchakkhu.viewmodels.AuthViewModel

@Composable
fun AppNavigation(authViewModel: AuthViewModel) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    Scaffold(
        floatingActionButton = {
            if (navBackStackEntry?.destination?.route in listOfNavItems.map { it.route }) {
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
                HomePage(navController, authViewModel)
            }
            composable(Routes.books_page) {
                BooksPage(navController, authViewModel)
            }
            composable(Routes.issue_page) {
                IssuePage() //navController, authViewModel
            }
            composable(Routes.submit_page) {
                SubmitPage()
            }
            composable(Routes.search_page) {
                SearchPage(navController, authViewModel)
            }
            composable(Routes.history_page) {
                RecordsPage(navController, authViewModel)
            }
            composable(Routes.profile_page) {
                ProfilePage(navController, authViewModel)
            }
        }
    }
}
