package com.example.tamanpempek.request

data class RegisterRequest(
    val name: String,
    val email: String,
    val password: String,
    val whatsapp: String,
    val gender: String,
    val role: String,
)
