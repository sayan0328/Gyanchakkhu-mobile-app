package com.example.gyanchakkhu.viewmodels

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@OptIn(FlowPreview::class)
class BooksViewModel: ViewModel(){
    private val database = Firebase.database.reference

    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    private val _isSearching = MutableStateFlow(false)
    val isSearching = _isSearching.asStateFlow()

    private val _isSearchBarEmpty = MutableStateFlow(true)
    val isSearchBarEmpty = _isSearchBarEmpty.asStateFlow()

    private val _isHistoryEmpty = MutableStateFlow(true)
    val isHistoryEmpty = _isHistoryEmpty.asStateFlow()

    private val _books = MutableStateFlow(listOf<Book>())

    //testing
    init{
        Log.d("BooksViewModel", "BooksViewModel initialized")
        viewModelScope.launch {
            _books.collect { books ->
                Log.d("BooksViewModel", "Books updated: $books")
            }
        }
    }
    //testing

    val books = searchText
        .debounce(500L)
        .onEach { _isSearching.update { true } }
        .combine(_books) { text, books ->
            Log.d("BooksViewModel", "Filtering books with text: $text")
            Log.d("BooksViewModel", "Current books list: $books")

            if (text.isBlank()) {
                _isSearchBarEmpty.update { true }
                Log.d("BooksViewModel", "Text is blank")
                books // Return all books when search is empty
            } else {
                _isSearchBarEmpty.update { false }
                books.filter {
                    val matches = it.doesMatchSearchQuery(text)
                    Log.d("BooksViewModel", "Book: ${it.bookName}, Matches: $matches")
                    matches
                }
            }
        }
        .onEach {
            _isSearching.update { false }
            Log.d("BooksViewModel", "Filtered Books: $it")
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

    fun onSearchTextChange(text: String) {
        _searchText.value = text
    }

    fun fetchBooks(libraryUid: String) {
        database.child("libraryList").child(libraryUid).child("bookList")
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val bookList = mutableListOf<Book>()
                    for(bookSnapshot in snapshot.children) {
                        val bookId = bookSnapshot.key ?: continue
                        val bookName = bookSnapshot.child("name").getValue(String::class.java) ?: "Not Found!!"
                        val libSection = bookSnapshot.child("librarySection").getValue(String::class.java) ?: "Not Found!!"
                        val rackNo = bookSnapshot.child("rackNo").getValue(String::class.java) ?: "Not Found!!"
                        bookList.add(Book(bookName, bookId, libSection, rackNo))
                    }
                    _books.value = bookList
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("Fetch Books", "Error fetching books from library: ${error.message}")
                }
            })
    }

    fun clearBookList() {
        _books.value = emptyList()
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

//private val booksList = listOf( //Predefined list of books for testing purposes
//    Book(
//        bookName = "ABC Book Dummy",
//        bookId = "123",
//        libSection = "A",
//        rackNo = "R1"
//    ),
//    Book(
//        bookName = "XYZ Book Dummy",
//        bookId = "456",
//        libSection = "B",
//        rackNo = "R2"
//    ),
//    Book(
//        bookName = "PQR Book Dummy",
//        bookId = "789",
//        libSection = "C",
//        rackNo = "R3"
//    ),
//    Book(
//        bookName = "MNO Book Dummy",
//        bookId = "678",
//        libSection = "D",
//        rackNo = "R4"
//    ),
//    Book(
//        bookName = "GHI Book Dummy",
//        bookId = "345",
//        libSection = "E",
//        rackNo = "R5"
//    ),
//)