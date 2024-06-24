package com.example.tamanpempek.helper

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns

fun getFileSize(uri: Uri, context: Context): Long {
    val contentResolver = context.contentResolver
    val cursor = contentResolver.query(uri, null, null, null, null)
    var size: Long = 0
    if (cursor != null && cursor.moveToFirst()) {
        val sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE)
        if (sizeIndex != -1) {
            size = cursor.getLong(sizeIndex)
        }
        cursor.close()
    }
    return size
}
