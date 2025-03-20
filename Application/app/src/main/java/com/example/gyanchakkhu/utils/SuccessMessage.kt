package com.example.gyanchakkhu.utils

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.gyanchakkhu.ui.theme.Blue40

@Composable
fun SuccessMessage(titleText: String, bodyText: String, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(titleText) },
        text = { Text(bodyText) },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = Blue40.copy(0.9f))
            ) {
                Text("OK")
            }
        }
    )
}