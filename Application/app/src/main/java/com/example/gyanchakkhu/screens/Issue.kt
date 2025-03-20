package com.example.gyanchakkhu.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.example.gyanchakkhu.R
import com.example.gyanchakkhu.utils.BookDetailsInIssueAndSubmit
import com.example.gyanchakkhu.utils.OneTimePopup
import com.example.gyanchakkhu.utils.SharedPrefs
import com.example.gyanchakkhu.utils.SuccessMessage
import com.example.gyanchakkhu.viewmodels.AuthViewModel
import com.example.gyanchakkhu.viewmodels.Book

@Composable
fun IssuePage(
    authViewModel: AuthViewModel,
    bookData: List<String>,
    onClick: () -> Unit,
    isVisible: Boolean
) {
    val context = LocalContext.current
    var showPopup by remember { mutableStateOf(SharedPrefs.isFirstTimeIssue(context)) }
    var showSuccessMessage by remember { mutableStateOf(false) }

    if (showPopup) {
        OneTimePopup(
            bodyText = stringResource(id = R.string.issue_message),
        ) { showPopup = false }
    }
    if(showSuccessMessage){
        SuccessMessage(
            titleText = "Success",
            bodyText = "Book Issued Successfully",
            onDismiss = { showSuccessMessage = false }
        )
    }
    Image(
        painter = painterResource(id = R.drawable.help_issue),
        contentDescription = "How to Issue",
        modifier = Modifier.clickable {
            onClick()
        }
    )
    BookDetailsInIssueAndSubmit(
        actionLabel = "Issue Now",
        bookName = bookData[0],
        bookId = bookData[1],
        librarySection = bookData[2],
        rackNo = bookData[3],
        onClick = {
            authViewModel.issueUserBook(
                Book(
                    bookName = bookData[0],
                    bookId = bookData[1],
                    libSection = bookData[2],
                    rackNo = bookData[3]
                ),
                onSuccess = {
                    showSuccessMessage = true
                }
            )

        },
        isVisible = isVisible
    )
}
