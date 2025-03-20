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
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDate
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

    private val _myBooks = MutableStateFlow<List<MyBook>>(emptyList())

    val myBooks = searchText
        .debounce(500L)
        .onEach { _isSearching.update { true } }
        .combine(_myBooks) { text, myBooks ->
            Log.d("BooksViewModel", "Filtering my books with text: $text")
            Log.d("BooksViewModel", "Current my books list: $myBooks")

            if (text.isBlank()) {
                _isSearchBarEmpty.update { true }
                Log.d("BooksViewModel", "Text is blank")
                myBooks // Return all my books when search is empty
            } else {
                _isSearchBarEmpty.update { false }
                myBooks.filter {
                    val matches = it.doesMatchSearchQuery(text)
                    Log.d("BooksViewModel", "My Book: ${it.bookName}, Matches: $matches")
                    matches
                }
            }
        }
        .onEach {
            _isSearching.update { false }
            Log.d("BooksViewModel", "Filtered My Books: $it")
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
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val bookList = mutableListOf<Book>()
                    for(bookSnapshot in snapshot.children) {
                        val bookId = bookSnapshot.key ?: continue
                        val bookName = bookSnapshot.child("name").getValue(String::class.java) ?: "Not Found!!"
                        val bookDesc = bookSnapshot.child("description").getValue(String::class.java) ?: "Not Found!!"
                        val libSection = bookSnapshot.child("librarySection").getValue(String::class.java) ?: "Not Found!!"
                        val rackNo = bookSnapshot.child("rackNo").getValue(String::class.java) ?: "Not Found!!"
                        bookList.add(Book(bookName, bookDesc, bookId, libSection, rackNo))
                    }
                    _books.value = bookList
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("Fetch Books", "Error fetching books from library: ${error.message}")
                }
            })
    }

    fun fetchMyBooks(userId: String, libraryUid: String, database: DatabaseReference) {
        viewModelScope.launch {
            database.child("userList").child(userId).child("myBooks").child(libraryUid)
                .addListenerForSingleValueEvent(object: ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val myBooksList = mutableListOf<MyBook>()
                        val bookIds = mutableListOf<String>()
                        for (bookSnapshot in snapshot.children){
                            val bookId = bookSnapshot.key ?: continue
                            val issueDate = bookSnapshot.child("issueDate")
                                .getValue(String::class.java) ?: "00/00/00"
                            val submitDate = bookSnapshot.child("submitDate")
                                .getValue(String::class.java) ?: "00/00/00"
                            myBooksList.add(MyBook(bookId = bookId, issueDate = issueDate, submitDate = submitDate))
                            bookIds.add(bookId)
                        }
                        if(bookIds.isNotEmpty()) {
                            fetchBookNames(libraryUid, bookIds, myBooksList, database)
                        }else{
                            _myBooks.value = myBooksList
                        }
                    }
                    override fun onCancelled(error: DatabaseError) {
                        Log.e("Fetch My Books", "Error fetching my books from library: ${error.message}")
                    }
                })
        }
    }

    private fun fetchBookNames(libraryUid: String, bookIds: List<String>, myBooksList: MutableList<MyBook>, database: DatabaseReference){
        database.child("libraryList").child(libraryUid).child("bookList")
            .addListenerForSingleValueEvent(object: ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    for(bookId in bookIds){
                        val bookSnapshot = snapshot.child(bookId)
                        if(bookSnapshot.exists()) {
                            val bookName = bookSnapshot.child("name").getValue(String::class.java) ?: "Not Found"
                            val myBookIndex = myBooksList.indexOfFirst {
                                it.bookId == bookId
                            }
                            if(myBookIndex != -1) {
                                myBooksList[myBookIndex]=myBooksList[myBookIndex].copy(bookName=bookName)
                            }
                        }
                    }
                    _myBooks.value = myBooksList
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("Fetch Book Names", "Error fetching book names: ${error.message}")
                }
            })
    }

    fun issueBook(book: Book, userId: String, libraryUid: String, database: DatabaseReference, onSuccess: () -> Unit) {
        val (issueDate, submitDate) = getIssueAndSubmitDate()
        database.child("userList").child(userId).child("myBooks").child(libraryUid).child(book.bookId).updateChildren(
            mapOf(
                "issueDate" to issueDate,
                "submitDate" to submitDate
            )
        )
            .addOnSuccessListener {
                database.child("libraryList").child(libraryUid).child("libraryUserList").child(userId).child(book.bookId).updateChildren(
                    mapOf(
                        "issueDate" to issueDate,
                        "submitDate" to submitDate
                    )
                )
                    .addOnSuccessListener {
                        fetchMyBooks(userId, libraryUid, database)
                        onSuccess()
                        Log.d("Issue Book", "Book issued successfully")
                    }
                    .addOnFailureListener {
                        Log.e("Issue Book", "Error issuing book: ${it.message}")
                    }
            }
            .addOnFailureListener {
                Log.e("Issue Book", "Error issuing book: ${it.message}")
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
    val bookName: String,
    val bookDesc: String = "",
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

data class MyBook(
    val bookName: String = "",
    val bookId: String,
    val issueDate: String,
    val submitDate: String
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