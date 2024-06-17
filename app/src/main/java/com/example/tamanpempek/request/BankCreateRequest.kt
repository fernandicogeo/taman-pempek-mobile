package com.example.tamanpempek.request

data class BankCreateRequest(
    val user_id: Int,
    val name: String,
    val type: String,
    val number: String,
)