package com.example.gyanchakkhu.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@OptIn(FlowPreview::class)
class BooksViewModel: ViewModel(){
    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    private val _isSearching = MutableStateFlow(false)
    val isSearching = _isSearching.asStateFlow()

    private val _isSearchBarEmpty = MutableStateFlow(true)
    val isSearchBarEmpty = _isSearchBarEmpty.asStateFlow()

    private val _books = MutableStateFlow(listOf<Book>())

    val books = searchText
        .debounce(500L)
        .onEach { _isSearching.update { true } }
        .combine(_books) { text, books ->
            if (text.isBlank()) {
                _isSearchBarEmpty.update { true }
                Log.d("BooksViewModel", "Text is blank")
                books // Returns all books when search is empty
            } else {
                _isSearchBarEmpty.update { false }
                books.filter {
                    it.doesMatchSearchQuery(text)
                }
            }
        }
        .onEach {
            _isSearching.update { false }
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

    private val _myBooks = MutableStateFlow<List<MyBook>>(emptyList())

    val myBooks = searchText
        .debounce(500L)
        .onEach { _isSearching.update { true } }
        .combine(_myBooks) { text, myBooks ->
            if (text.isBlank()) {
                _isSearchBarEmpty.update { true }
                Log.d("BooksViewModel", "Text is blank")
                myBooks // Returns all my books when search is empty
            } else {
                _isSearchBarEmpty.update { false }
                myBooks.filter {
                    it.doesMatchSearchQuery(text)
                }
            }
        }
        .onEach {
            _isSearching.update { false }
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

    fun onSearchTextChange(text: String) {
        _searchText.value = text
    }

    fun clearSearchText(){
        _searchText.value=""
    }

    fun fetchBooks(libraryUid: String, database: DatabaseReference) {
        database.child("libraryList").child(libraryUid).child("bookList")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val bookList = snapshot.children.mapNotNull { bookSnapshot ->
                        val bookId = bookSnapshot.key ?: return@mapNotNull null
                        Book(
                            author = bookSnapshot.child("author").getValue(String::class.java) ?: "",
                            bookId = bookId,
                            bookName = bookSnapshot.child("bookName").getValue(String::class.java) ?: "",
                            coverImage = bookSnapshot.child("coverImage").getValue(String::class.java) ?: "",
                            description = bookSnapshot.child("description").getValue(String::class.java) ?: "",
                            edition = bookSnapshot.child("edition").getValue(String::class.java) ?: "",
                            genre = bookSnapshot.child("genre").getValue(String::class.java) ?: "",
                            isbnNo = bookSnapshot.child("isbnNo").getValue(String::class.java) ?: "",
                            language = bookSnapshot.child("language").getValue(String::class.java) ?: "",
                            librarySection = bookSnapshot.child("librarySection").getValue(String::class.java) ?: "",
                            pageCount = bookSnapshot.child("pageCount").getValue(String::class.java) ?: "",
                            publicationYear = bookSnapshot.child("publicationYear").getValue(String::class.java) ?: "",
                            publisher = bookSnapshot.child("publisher").getValue(String::class.java) ?: "",
                            rackNo = bookSnapshot.child("rackNo").getValue(String::class.java) ?: ""
                        )
                    }
                    _books.value = bookList
                }
                override fun onCancelled(error: DatabaseError) {
                    Log.e("Fetch Books", "Error fetching books: ${error.message}")
                }
            })
    }

    fun fetchMyBooks(userId: String, libraryUid: String, database: DatabaseReference) {
        database.child("userList").child(userId).child("myBooks").child(libraryUid)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val myBooksList = mutableListOf<MyBook>()

                    for (bookSnapshot in snapshot.children) {
                        val bookId = bookSnapshot.key ?: continue
                        val issueDate = bookSnapshot.child("issueDate").getValue(String::class.java) ?: "00/00/00"
                        val submitDate = bookSnapshot.child("submitDate").getValue(String::class.java) ?: "00/00/00"
                        val isSubmitted = bookSnapshot.child("isSubmitted").getValue(Boolean::class.java) ?: false
                        val book = _books.value.find { it.bookId == bookId }
                        myBooksList.add(MyBook(
                            bookId = bookId,
                            bookName = book?.bookName ?: "Not Found!",
                            author = book?.author ?: "Not Found!",
                            coverImage = book?.coverImage ?: "",
                            issueDate = issueDate,
                            submitDate = submitDate,
                            isSubmitted = isSubmitted
                        ))
                    }
                    _myBooks.value = myBooksList
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("Fetch My Books", "Error fetching my books: ${error.message}")
                }
            })
    }

    fun issueBook(bookId: String, bookName: String, userId: String, libraryUid: String, database: DatabaseReference, onSuccess: () -> Unit) {
        val (issueDate, submitDate) = getIssueAndSubmitDate()

        val updates = mapOf(
            "/userList/$userId/myBooks/$libraryUid/${bookId}/issueDate" to issueDate,
            "/userList/$userId/myBooks/$libraryUid/${bookId}/submitDate" to submitDate,
            "/userList/$userId/myBooks/$libraryUid/${bookId}/isSubmitted" to false,
            "/libraryList/$libraryUid/libraryUserList/$userId/${bookId}/bookName" to bookName,
            "/libraryList/$libraryUid/libraryUserList/$userId/${bookId}/issueDate" to issueDate,
            "/libraryList/$libraryUid/libraryUserList/$userId/${bookId}/submitDate" to submitDate,
            "/libraryList/$libraryUid/libraryUserList/$userId/${bookId}/isSubmitted" to false
        )

        database.updateChildren(updates)
            .addOnSuccessListener {
                fetchMyBooks(userId, libraryUid, database)
                onSuccess()
            }
            .addOnFailureListener {
                Log.e("Issue Book", "Error issuing book: ${it.message}")
            }
    }

    fun submitBook(bookId: String, userId: String, libraryUid: String, database: DatabaseReference, onSuccess: () -> Unit) {
        val updates = mapOf(
            "/userList/$userId/myBooks/$libraryUid/${bookId}/isSubmitted" to true,
            "/libraryList/$libraryUid/libraryUserList/$userId/${bookId}/isSubmitted" to true
        )

        database.updateChildren(updates)
            .addOnSuccessListener {
                fetchMyBooks(userId, libraryUid, database)
                onSuccess()
            }
            .addOnFailureListener {
                Log.e("Submit Book", "Error submitting book: ${it.message}")
            }
    }

    fun clearBookList() {
        _books.value = emptyList()
    }

    fun clearMyBookList() {
        _myBooks.value = emptyList()
    }

    private fun getIssueAndSubmitDate(): Pair<String, String> {
        val dateFormat = SimpleDateFormat("dd/MM/yy", Locale.getDefault())

        val issueCalendar = Calendar.getInstance()
        val issueDate = dateFormat.format(issueCalendar.time)

        val submitCalendar = Calendar.getInstance()
        submitCalendar.add(Calendar.DAY_OF_YEAR, 7)
        val submitDate = dateFormat.format(submitCalendar.time)

        return Pair(issueDate, submitDate)
    }
}

data class Book(
    val author: String = "",
    val bookId: String = "",
    val bookName: String = "",
    val coverImage: String = "",
    val description: String = "",
    val edition: String = "",
    val genre: String = "",
    val isbnNo: String = "",
    val language: String = "",
    val librarySection: String = "",
    val pageCount: String = "",
    val publicationYear: String = "",
    val publisher: String = "",
    val rackNo: String = ""
) {
    fun doesMatchSearchQuery(query: String): Boolean {
        val matchingCombination = listOf(
            bookName,
            author,
            genre
        )
        return matchingCombination.any{
            it.contains(query, ignoreCase = true)
        }
    }
}

data class MyBook(
    val bookName: String = "",
    val author: String = "",
    val coverImage: String = "",
    val bookId: String,
    val issueDate: String,
    val submitDate: String,
    val isSubmitted: Boolean
) {
    fun doesMatchSearchQuery(query: String): Boolean {
        val matchingCombination = listOf(
            "$bookName$bookId",
            "$bookName $bookId",
            issueDate,
            submitDate
        )
        return matchingCombination.any{
            it.contains(query, ignoreCase = true)
        }
    }
}