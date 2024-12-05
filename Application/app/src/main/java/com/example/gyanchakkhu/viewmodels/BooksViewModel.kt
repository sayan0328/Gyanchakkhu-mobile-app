package com.example.gyanchakkhu.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class BooksViewModel: ViewModel(){
    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    private val _isSearching = MutableStateFlow(false)
    val isSearching = _isSearching.asStateFlow()

    private val _books = MutableStateFlow(booksList) //listOf<Book>()
    val books = searchText
        .combine(_books) { text, books ->
            if(text.isBlank()){
                books
            } else {
                books.filter {
                    it.doesMatchSearchQuery(text)
                }
            }
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            _books.value
        )

    fun onSearchTextChange(text: String) {
        _searchText.value = text
    }
}

data class Book(
    val name: String,
    val id: String,
    val libSection: String,
    val rackNo: Int
) {
    fun doesMatchSearchQuery(query: String): Boolean {
        val matchingCombination = listOf(
            "$name$id",
            "$name $id",
            "$libSection$rackNo",
            "$libSection $rackNo"
        )
        return matchingCombination.any{
            it.contains(query, ignoreCase = true)
            return true
        }
    }
}

private val booksList = listOf( //Predefined list of books for testing purposes
    Book(
        name = "ABC",
        id = "123",
        libSection = "A",
        rackNo = 1
    ),
    Book(
        name = "XYZ",
        id = "456",
        libSection = "B",
        rackNo = 2
    ),
    Book(
        name = "PQR",
        id = "789",
        libSection = "C",
        rackNo = 3
    ),
    Book(
        name = "MNO",
        id = "678",
        libSection = "D",
        rackNo = 4
    ),
    Book(
        name = "GHI",
        id = "345",
        libSection = "E",
        rackNo = 5
    ),
)