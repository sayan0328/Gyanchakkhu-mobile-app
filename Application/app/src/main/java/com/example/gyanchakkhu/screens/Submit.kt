package com.example.gyanchakkhu.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.gyanchakkhu.R
import com.example.gyanchakkhu.ui.theme.poppinsFontFamily
import com.example.gyanchakkhu.utils.BookDetailsInIssueAndSubmit
import com.example.gyanchakkhu.utils.PopupDialog
import com.example.gyanchakkhu.utils.SharedPrefs
import com.example.gyanchakkhu.viewmodels.AuthViewModel
import com.example.gyanchakkhu.viewmodels.Book

@Composable
fun SubmitPage(
    authViewModel: AuthViewModel,
    book: Book,
    onQrClick: () -> Unit,
    onBookClick: () -> Unit,
    clearData: () -> Unit,
    closeCamera: () -> Unit,
    isVisible: Boolean
) {
    val context = LocalContext.current
    var showPopup by remember { mutableStateOf(SharedPrefs.isFirstTimeSubmit(context)) }
    var showSuccessMessage by remember { mutableStateOf(false) }
    var underDev by remember { mutableStateOf(false) }

    if (showPopup) {
        PopupDialog(
            bodyText = stringResource(id = R.string.submit_message),
        ) { showPopup = false }
    }
    if (showSuccessMessage) {
        PopupDialog(
            titleText = "Success",
            bodyText = "Book Submitted Successfully",
            onDismiss = { showSuccessMessage = false }
        )
    }
    if (underDev) {
        PopupDialog(
            titleText = "This feature is currently under development",
            onDismiss = {
                underDev = false
                closeCamera()
            }
        )
    }
    if (isVisible) {
        Dialog(
            onDismissRequest = {
                clearData()
            }, properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            BookDetailsInIssueAndSubmit(
                actionLabel = "Submit Now",
                book = book,
                onClick = {
                    authViewModel.submitUserBook(
                        book.bookId,
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
    Spacer(modifier = Modifier.height(12.dp))
    Image(
        painter = painterResource(id = R.drawable.scan_qr_body),
        contentDescription = "Scan QR Body",
        modifier = Modifier
            .clickable {
                onQrClick()
            }
    )
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp)
    ) {
        Divider(
            color = Color.Gray,
            thickness = 1.dp,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = "And",
            fontFamily = poppinsFontFamily,
            fontSize = 14.sp,
            fontStyle = FontStyle.Italic,
            modifier = Modifier.padding(horizontal = 16.dp),
            color = Color.Black
        )
        Divider(
            color = Color.Gray,
            thickness = 1.dp,
            modifier = Modifier.weight(1f)
        )
    }
    Image(
        painter = painterResource(id = R.drawable.scan_book_top),
        contentDescription = "Scan Book Top",
    )
    Image(
        painter = painterResource(id = R.drawable.scan_book_body),
        contentDescription = "Scan Book Body",
        modifier = Modifier
            .clickable {
                onBookClick()
                underDev = true
            }
    )
}