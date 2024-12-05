package com.example.gyanchakkhu.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.example.gyanchakkhu.ui.theme.Blue120
import com.example.gyanchakkhu.ui.theme.Blue160

@Composable
fun gradientBrush(
    colorStops: Array<Pair<Float, Color>> = arrayOf(
        0.0f to Blue160,
        0.6f to Blue120
    ),
    start: Offset = Offset(0f, 0f),
    end: Offset = Offset(1000f, 1000f)
): Brush {
    return Brush.linearGradient(
        colorStops = colorStops,
        start = start,
        end = end
    )
}


