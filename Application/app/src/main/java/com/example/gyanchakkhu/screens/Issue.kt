package com.example.gyanchakkhu.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.gyanchakkhu.R
import com.example.gyanchakkhu.ui.theme.MyPurple100
import com.example.gyanchakkhu.ui.theme.poppinsFontFamily
import com.example.gyanchakkhu.utils.BookDetailsInIssueAndSubmit
import com.example.gyanchakkhu.utils.PopupDialog
import com.example.gyanchakkhu.utils.SharedPrefs
import com.example.gyanchakkhu.viewmodels.AuthViewModel
import com.example.gyanchakkhu.viewmodels.Book

@Composable
fun IssuePage(
    authViewModel: AuthViewModel,
    book: Book,
    onClick: () -> Unit,
    clearData: () -> Unit,
    isVisible: Boolean
) {
    val context = LocalContext.current
    var showPopup by remember { mutableStateOf(SharedPrefs.isFirstTimeIssue(context)) }
    var showSuccessMessage by remember { mutableStateOf(false) }

    if (showPopup) {
        PopupDialog(
            bodyText = stringResource(id = R.string.issue_message),
        ) { showPopup = false }
    }
    if(showSuccessMessage){
        PopupDialog(
            titleText = "Success",
            bodyText = "Book Issued Successfully",
            onDismiss = { showSuccessMessage = false }
        )
    }
    if(isVisible){
        Dialog(
            onDismissRequest = {
                clearData()
            }
            ,properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            BookDetailsInIssueAndSubmit(
                actionLabel = "Issue Now",
                book = book,
                onClick = {
                    authViewModel.issueUserBook(
                        book.bookId,
                        book.bookName,
                        onSuccess = {
                            showSuccessMessage = true
                            clearData()
                        }
                    )
                },
                onClose = {
                    clearData()
                }
            )
        }
    }
    Spacer(modifier = Modifier.height(24.dp))
    Image(
        painter = painterResource(id = R.drawable.scan_qr_top),
        contentDescription = "Scan QR Top",
    )
    Spacer(modifier = Modifier.height(56.dp))
    Image(
        painter = painterResource(id = R.drawable.scan_qr_body),
        contentDescription = "Scan QR Body",
        modifier = Modifier
            .size(200.dp)
            .clickable {
            onClick()
        }
    )
    Spacer(modifier = Modifier.height(8.dp))
    Column (
        modifier = Modifier
            .height(28.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.issue_help),
            color = MyPurple100,
            fontFamily = poppinsFontFamily,
            fontSize = 12.sp,
            modifier = Modifier.padding(horizontal = 12.dp)
        )
    }
}
