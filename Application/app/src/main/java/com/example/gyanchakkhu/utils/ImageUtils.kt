package com.example.gyanchakkhu.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64

fun decodeBase64ToBitmap(base64Str: String): Bitmap? {
    return try {
        val cleanBase64 = base64Str.substringAfter("base64,", base64Str)
        val decodedBytes = Base64.decode(cleanBase64, Base64.DEFAULT)
        BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
    } catch (e: IllegalArgumentException) {
        null
    }
}