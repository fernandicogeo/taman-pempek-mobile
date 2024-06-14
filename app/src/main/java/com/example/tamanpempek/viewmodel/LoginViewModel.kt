package com.example.tamanpempek.viewmodel

import androidx.lifecycle.ViewModel
import com.example.tamanpempek.repository.UserRepository

class LoginViewModel(private val repository: UserRepository) : ViewModel() {
    fun login(userEmail: String, userPassword: String) = repository.login(userEmail, userPassword)
}