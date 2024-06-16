package com.example.tamanpempek.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.tamanpempek.repository.BankRepository

class BankViewModel (private val repository: BankRepository) : ViewModel() {
    fun getBanks() = repository.getBanks()
    fun getBankById(userId: Int) = repository.getBankById(userId)
    fun createBank(name: String, type: String, number: String) = repository.createBank(name, type, number)

}