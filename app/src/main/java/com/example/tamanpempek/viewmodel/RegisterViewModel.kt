package com.example.tamanpempek.viewmodel

import androidx.lifecycle.ViewModel
import com.example.tamanpempek.repository.UserRepository

class RegisterViewModel(private val repository: UserRepository) : ViewModel() {
    fun register(userName: String, userEmail: String, userPassword: String, userWhatsapp: String, userGender: String, userRole: String) = repository.register(userName, userEmail, userPassword, userWhatsapp, userGender, userRole)
}