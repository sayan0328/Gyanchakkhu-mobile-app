package com.example.gyanchakkhu.screens

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.example.gyanchakkhu.R
import com.example.gyanchakkhu.utils.BookDetails
import com.example.gyanchakkhu.utils.OneTimePopup
import com.example.gyanchakkhu.utils.SharedPrefs

@Composable
fun IssuePage() {
    val context = LocalContext.current
    var showPopup by remember { mutableStateOf(SharedPrefs.isFirstTimeIssue(context)) }

    if (showPopup) {
        OneTimePopup(
            bodyText = stringResource(id = R.string.issue_message),
        ) { showPopup = false }
    }

    Image(
        painter = painterResource(id = R.drawable.help_issue),
        contentDescription = "How to Issue"
    )
    BookDetails(actionLabel = "Issue Now")
}
