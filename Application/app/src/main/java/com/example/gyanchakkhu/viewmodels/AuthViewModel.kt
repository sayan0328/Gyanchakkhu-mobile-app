package com.example.gyanchakkhu.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.UUID

class AuthViewModel(
    private val booksViewModel: BooksViewModel
) : ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val database = Firebase.database.reference

    private val _authState = MutableLiveData<AuthState>(AuthState.Unauthenticated)
    val authState: LiveData<AuthState> = _authState

    private val _userData = MutableLiveData<UserData>(UserData())
    val userData: LiveData<UserData> = _userData

    private val _isEnrolledInLibrary = MutableLiveData<Boolean>(false)
    val isEnrolledInLibrary: LiveData<Boolean> = _isEnrolledInLibrary

    private val _libraryName = MutableLiveData<String>("Not Found!")
    val libraryName: LiveData<String> = _libraryName

    init {
        checkAuthStatus()
    }

    fun checkAuthStatus() {
        val currentUser = auth.currentUser
        _authState.value = AuthState.Loading
        if (currentUser == null) {
            _authState.value = AuthState.Unauthenticated
            _isEnrolledInLibrary.value = false
        } else {
            val userId = currentUser.uid
            database.child("userList").child(userId).get()
                .addOnSuccessListener { snapshot ->
                    if (snapshot.exists()) {
                        fetchUserData(userId) { user ->
                            _isEnrolledInLibrary.value = user.libraryUid != null
                            user.libraryUid?.let {
                                getLibraryName(it)
                                booksViewModel.fetchBooks(it, database)
                                booksViewModel.fetchMyBooks(userId, it, database)

                            }
                            _authState.value = AuthState.Authenticated
                        }
                    } else {
                        _authState.value = AuthState.Unauthenticated
                    }
                }
                .addOnFailureListener { e ->
                    Log.e("FirebaseCheckAuth", "Error checking authentication status: ${e.message}")
                    _authState.value = AuthState.Error("Error fetching user data")
                }
        }
    }


    fun login(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            _authState.value = AuthState.Error("Email and password cannot be empty")
            return
        }
        _authState.value = AuthState.Loading
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    checkAuthStatus()
                    _authState.value = AuthState.Authenticated
                } else {
                    _authState.value = AuthState.Error(task.exception?.message ?: "Login failed")
                }
            }
    }

    fun signup(name: String, email: String, password: String, confirmPassword: String) {
        when {
            name.isBlank() -> _authState.value = AuthState.Error("Name is required")
            email.isBlank() -> _authState.value = AuthState.Error("Email cannot be empty")
            password.isBlank() -> _authState.value = AuthState.Error("Password cannot be empty")
            password != confirmPassword -> _authState.value = AuthState.Error("Passwords do not match")
            else -> {
                _authState.value = AuthState.Loading
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            auth.currentUser?.uid?.let { userId ->
                                val user = UserData(name = name, email = email)
                                database.child("userList").child(userId).setValue(user)
                                    .addOnSuccessListener {
                                        _userData.value = user
                                        _authState.value = AuthState.Authenticated
                                    }
                                    .addOnFailureListener { e ->
                                        _authState.value = AuthState.Error(e.message ?: "Signup failed")
                                    }
                            }
                        } else {
                            _authState.value = AuthState.Error(task.exception?.message ?: "Signup failed")
                        }
                    }
            }
        }
    }

    fun signout() {
        auth.signOut()
        _authState.value = AuthState.Unauthenticated
        _userData.value = UserData()
        _isEnrolledInLibrary.value = false
        _libraryName.value = "Not Found!"
        booksViewModel.clearBookList()
        booksViewModel.clearMyBookList()
    }

    private fun fetchUserData(userId: String, onComplete: (UserData) -> Unit = {}) {
        database.child("userList").child(userId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val userDetails = snapshot.getValue(UserData::class.java) ?: UserData()
                    _userData.value = userDetails
                    onComplete(userDetails)
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("FirebaseFetchData", "Error fetching user data: ${error.message}")
                    onComplete(UserData())
                }
            })
    }

    private fun getLibraryName(libraryUid: String) {
        database.child("libraryList").child(libraryUid).child("name").get()
            .addOnSuccessListener { snapshot ->
                _libraryName.value = snapshot.value.toString()
            }
            .addOnFailureListener { e ->
                Log.e("FirebaseGetLibName", "Error fetching library name: ${e.message}")
            }
    }

    fun registerAsLibraryUser(libraryName: String, libraryUid: String) {
        val userId = auth.currentUser?.uid ?: return
        val user = _userData.value ?: return
        val cardUid = generateUniqueCardId()

        database.child("libraryList").child(libraryUid).child("name").get()
            .addOnSuccessListener { snapshot ->
                if (snapshot.value == libraryName) {
                    database.child("libraryList").child(libraryUid).child("libraryUserList").child(userId).setValue(true)
                        .addOnSuccessListener {
                            database.child("userList").child(userId).updateChildren(
                                mapOf("libraryUid" to libraryUid, "cardUid" to cardUid)
                            ).addOnSuccessListener {
                                _isEnrolledInLibrary.value = true
                                _userData.value = user.copy(libraryUid = libraryUid, cardUid = cardUid)
                                getLibraryName(libraryUid)
                                booksViewModel.fetchBooks(libraryUid, database)
                                booksViewModel.fetchMyBooks(userId, libraryUid, database)
                                Log.d("FirebaseEnroll", "Library user registered successfully")
                            }.addOnFailureListener { e ->
                                Log.e("FirebaseEnroll", "Could not update library user data: ${e.message}")
                            }
                        }
                        .addOnFailureListener { e ->
                            Log.e("FirebaseEnroll", "Could not register as library user: ${e.message}")
                        }
                } else {
                    _authState.value = AuthState.Error("Library name and ID do not match")
                }
            }
            .addOnFailureListener { e ->
                Log.e("FirebaseEnroll", "Error fetching library data: ${e.message}")
            }
    }

    private fun generateUniqueCardId(): String = UUID.randomUUID().toString()
}

data class UserData(
    val name: String? = null,
    val email: String? = null,
    val cardUid: String? = null,
    val libraryUid: String? = null
)

sealed class AuthState {
    object Authenticated : AuthState()
    object Unauthenticated : AuthState()
    object Loading : AuthState()
    data class Error(val message: String) : AuthState()
}
