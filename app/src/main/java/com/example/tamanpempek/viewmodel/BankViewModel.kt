package com.example.tamanpempek.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.tamanpempek.repository.BankRepository

class BankViewModel (private val repository: BankRepository) : ViewModel() {
    fun getBanks() = repository.getBanks()
}