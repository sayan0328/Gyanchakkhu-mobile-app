package com.example.gyanchakkhu.utils

import com.example.gyanchakkhu.R

data class NavItems (
    val label: String,
    val icon: Int,
    val route: String
)

val listOfNavItems: List<NavItems> = listOf(
    NavItems(
        label = "Books",
        icon = R.drawable.books,
        route = Routes.books_page
    ),
    NavItems(
        label = "Search",
        icon = R.drawable.search,
        route = Routes.search_page
    ),
    NavItems(
        label = "Home",
        icon = R.drawable.home,
        route = Routes.home_page
    ),
    NavItems(
        label = "History",
        icon = R.drawable.records,
        route = Routes.history_page
    ),
    NavItems(
        label = "Profile",
        icon = R.drawable.profile,
        route = Routes.profile_page
    )
)