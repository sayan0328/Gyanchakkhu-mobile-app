package com.example.gyanchakkhu.utils

import android.widget.Space
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.gyanchakkhu.ui.theme.poppinsFontFamily
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun CheckLocationDialog(
    showDialog: Boolean,
    onDismiss: () -> Unit,
) {
    if(showDialog) {
        val messages = listOf("Loading...", "Fetching location...", "Almost done...")
        val delays = listOf(500L, 2000L, 1000L)
        var currentMessage by remember { mutableStateOf(messages[0]) }
        val coroutineScope = rememberCoroutineScope()

        LaunchedEffect(Unit) {
            coroutineScope.launch {
                for(i in 0..2) {
                    currentMessage = messages[i]
                    delay(delays[i])
                }
                delay(500)
                onDismiss()
            }
        }

        Dialog(
            onDismissRequest = { Unit },
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            Box (
                modifier = Modifier
                    .height(150.dp)
                    .width(200.dp)
                    .background(Color.White, shape = RoundedCornerShape(16.dp))
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator()
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = currentMessage,
                        fontFamily = poppinsFontFamily,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}