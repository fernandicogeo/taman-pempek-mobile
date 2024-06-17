package com.example.tamanpempek.request

data class CartCreateRequest (
    val user_id: Int,
    val product_id: Int,
    val payment_id: Int?,
    val quantity: Int,
    val total_price: Int,
    val isActived: String, // actived, canceled, paid
)