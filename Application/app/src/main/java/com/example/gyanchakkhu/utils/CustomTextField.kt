package com.example.gyanchakkhu.utils

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun CustomTextField(modifier: Modifier = Modifier, text: String, onValueChange: (String) -> Unit, label: String) {
    var isFocused by remember { mutableStateOf(false) }

    BasicTextField(
        value = text,
        onValueChange = onValueChange,
        singleLine = true,
        modifier = Modifier
            .fillMaxWidth()
            .onFocusChanged { isFocused = it.isFocused },
        decorationBox = { innerTextField ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(20.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                if (text.isEmpty()) {
                    Text(
                        text = label,
                        color = Color.Gray
                    )
                }
                innerTextField()
            }
        }
    )
    Box(modifier = Modifier
        .fillMaxWidth()
        .padding(top = 2.dp)) {
        Divider(
            color = if (isFocused) Color.Blue else Color.LightGray,
            thickness = 2.dp
        )
    }
}