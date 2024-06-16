package com.example.tamanpempek.viewmodel.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.tamanpempek.helper.Injection
import com.example.tamanpempek.repository.BankRepository
import com.example.tamanpempek.viewmodel.BankViewModel

class BankViewModelFactory private constructor(private val repository: BankRepository) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BankViewModel::class.java)) {
            return BankViewModel(repository) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: BankViewModelFactory? = null
        fun getInstanceBank(context: Context): BankViewModelFactory {
            return instance ?: synchronized(this) {
                instance ?: BankViewModelFactory(Injection.provideRepositoryBank(context))
            }.also { instance = it }
        }
    }
}