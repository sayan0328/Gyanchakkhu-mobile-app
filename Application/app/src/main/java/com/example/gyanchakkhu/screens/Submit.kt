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

@Composable
fun SubmitPage(
    bookData: List<String>,
    onClick: () -> Unit,
    isVisible: Boolean
) {
    val context = LocalContext.current
    var showPopup by remember { mutableStateOf(SharedPrefs.isFirstTimeSubmit(context)) }

    if (showPopup) {
        OneTimePopup(
            bodyText = stringResource(id = R.string.submit_message),
        ) { showPopup = false }
    }
    Image(
        painter = painterResource(id = R.drawable.help_submit),
        contentDescription = "How to Submit",
        modifier = Modifier.clickable {
            onClick()
        }
    )
    BookDetailsInIssueAndSubmit(
        actionLabel = "Submit Now",
        bookName = bookData[0],
        bookId = bookData[1],
        librarySection = bookData[2],
        rackNo = bookData[3],
        isVisible = isVisible
    )
}