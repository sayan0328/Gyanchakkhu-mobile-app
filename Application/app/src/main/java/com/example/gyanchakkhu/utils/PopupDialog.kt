package com.example.gyanchakkhu.utils

import android.content.Context
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.gyanchakkhu.ui.theme.Blue40

@Composable
fun PopupDialog(
    titleText: String = "Welcome!",
    bodyText: String = "",
    onDismiss: () -> Unit
) {
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

object SharedPrefs {
    private const val PREFS_NAME = "my_prefs"
    private const val KEY_FIRST_TIME_ISSUE = "first_time_issue"
    private const val KEY_FIRST_TIME_SUBMIT = "first_time_submit"

    fun isFirstTimeIssue(context: Context): Boolean {
        return isFirstTime(context, KEY_FIRST_TIME_ISSUE)
    }

    fun isFirstTimeSubmit(context: Context): Boolean {
        return isFirstTime(context, KEY_FIRST_TIME_SUBMIT)
    }

    private fun isFirstTime(context: Context, key: String): Boolean {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val isFirstTime = prefs.getBoolean(key, true)
        if (isFirstTime) {
            prefs.edit().putBoolean(key, false).apply()
        }
        return isFirstTime
    }
}