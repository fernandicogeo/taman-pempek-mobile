package com.example.tamanpempek.request

import android.net.Uri

data class PaymentCreateRequest (
    val user_id: Int,
    val delivery_id: Int?,
    val total_price: Int,
    val image: Uri,
    val address: String,
    val whatsapp: String,
    val payment_status: String,
    val delivery_name: String?,
)