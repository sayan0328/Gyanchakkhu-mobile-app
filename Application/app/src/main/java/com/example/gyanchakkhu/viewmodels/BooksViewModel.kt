package com.example.gyanchakkhu.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

@OptIn(FlowPreview::class)
class BooksViewModel: ViewModel(){
    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    private val _isSearching = MutableStateFlow(false)
    val isSearching = _isSearching.asStateFlow()

    private val _isSearchBarEmpty = MutableStateFlow(true)
    val isSearchBarEmpty = _isSearchBarEmpty.asStateFlow()

    private val _isHistoryEmpty = MutableStateFlow(false) //Should be initially true
    val isHistoryEmpty = _isHistoryEmpty.asStateFlow()

    private val _books = MutableStateFlow(booksList) //listOf<Book>()
    val books = searchText
        .debounce(500L)
        .onEach { _isSearching.update { true } }
        .combine(_books) { text, books ->
            if(text.isBlank()){
                _isSearchBarEmpty.update { true }
                books
            } else {
                _isSearchBarEmpty.update { false }
                delay(2000L)
                books.filter {
                    it.doesMatchSearchQuery(text)
                }
            }
        }
        .onEach { _isSearching.update { false } }
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
    val bookName: String,
    val bookId: String,
    val libSection: String,
    val rackNo: String
) {
    fun doesMatchSearchQuery(query: String): Boolean {
        val matchingCombination = listOf(
            "$bookName$bookId",
            "$bookName $bookId",
            "$libSection$rackNo",
            "$libSection $rackNo"
        )
        return matchingCombination.any{
            it.contains(query, ignoreCase = true)
        }
    }
}

private val booksList = listOf( //Predefined list of books for testing purposes
    Book(
        bookName = "ABC Book",
        bookId = "123",
        libSection = "A",
        rackNo = "R1"
    ),
    Book(
        bookName = "XYZ Book",
        bookId = "456",
        libSection = "B",
        rackNo = "R2"
    ),
    Book(
        bookName = "PQR Book",
        bookId = "789",
        libSection = "C",
        rackNo = "R3"
    ),
    Book(
        bookName = "MNO Book",
        bookId = "678",
        libSection = "D",
        rackNo = "R4"
    ),
    Book(
        bookName = "GHI Book",
        bookId = "345",
        libSection = "E",
        rackNo = "R5"
    ),
)