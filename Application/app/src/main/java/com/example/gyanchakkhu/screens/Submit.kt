package com.example.gyanchakkhu.screens

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.example.gyanchakkhu.R
import com.example.gyanchakkhu.utils.BookDetails
import com.example.gyanchakkhu.utils.DropdownMenu
import com.example.gyanchakkhu.utils.OneTimePopup
import com.example.gyanchakkhu.utils.SharedPrefs

@Composable
fun SubmitPage() {
    val context = LocalContext.current
    var showPopup by remember { mutableStateOf(SharedPrefs.isFirstTimeSubmit(context)) }

    if (showPopup) {
        OneTimePopup(
            bodyText = stringResource(id = R.string.submit_message),
        ) { showPopup = false }
    }
    DropdownMenu()
    Image(
        painter = painterResource(id = R.drawable.help_submit),
        contentDescription = "How to Submit"
    )
    BookDetails(actionLabel = "Submit Now")
}