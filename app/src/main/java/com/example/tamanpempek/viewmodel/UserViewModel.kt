package com.example.tamanpempek.viewmodel

import androidx.lifecycle.ViewModel
import com.example.tamanpempek.repository.UserRepository

class UserViewModel(private val repository: UserRepository) : ViewModel() {
    fun login(userEmail: String, userPassword: String) = repository.login(userEmail, userPassword)
    fun register(userName: String, userEmail: String, userPassword: String, userWhatsapp: String, userGender: String, userRole: String) = repository.register(userName, userEmail, userPassword, userWhatsapp, userGender, userRole)
    fun logout() = repository.logout()
    fun getUserById(userId: Int) = repository.getUserById(userId)
    fun updateUser(id: Int, userName: String, userEmail: String, userPassword: String, userWhatsapp: String, userGender: String) = repository.updateUser(id, userName, userEmail, userPassword, userWhatsapp, userGender)
}