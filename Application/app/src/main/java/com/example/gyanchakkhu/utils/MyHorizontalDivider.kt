package com.example.gyanchakkhu.utils

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun MyHorizontalDivider(modifier: Modifier = Modifier) {
    HorizontalDivider(
        modifier = Modifier.padding(horizontal = 36.dp, vertical = 12.dp),
        thickness = 0.6.dp,
        color = Color.Black
    )
}