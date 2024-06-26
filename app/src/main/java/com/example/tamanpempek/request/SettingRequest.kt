package com.example.tamanpempek.request

import android.net.Uri

data class SettingRequest (
    val image: Uri?,
    val description: String,
    val contact: String,
)