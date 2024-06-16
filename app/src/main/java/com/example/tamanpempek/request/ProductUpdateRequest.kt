package com.example.tamanpempek.request

import android.net.Uri

data class ProductUpdateRequest(
    val user_id: Int,
    val category_id: Int,
    val name: String,
    val image: Uri?,
    val description: String,
    val price: Int,
    val stock: Int
)