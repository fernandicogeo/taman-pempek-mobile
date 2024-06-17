package com.example.tamanpempek.request

data class UserUpdateRequest(
    val name: String,
    val email: String,
    val password: String,
    val whatsapp: String,
    val gender: String,
)
