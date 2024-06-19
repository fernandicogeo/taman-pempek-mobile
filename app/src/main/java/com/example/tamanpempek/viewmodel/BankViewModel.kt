package com.example.tamanpempek.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.tamanpempek.repository.BankRepository

class BankViewModel (private val repository: BankRepository) : ViewModel() {
    fun getBanks() = repository.getBanks()
    fun getBanksByUser(userId: Int) = repository.getBanksByUser(userId)
    fun getBankById(userId: Int) = repository.getBankById(userId)
    fun getAdminBanks() = repository.getAdminBanks()
    fun createBank(userId: Int, name: String, type: String, number: String) = repository.createBank(userId, name, type, number)
    fun updateBank(id: Int, name: String, type: String, number: String) = repository.updateBank(id, name, type, number)
    fun deleteBank(id: Int) = repository.deleteBank(id)
}