package com.example.tamanpempek.viewmodel.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.tamanpempek.helper.Injection
import com.example.tamanpempek.repository.PaymentRepository
import com.example.tamanpempek.viewmodel.PaymentViewModel

class PaymentViewModelFactory private constructor(private val repository: PaymentRepository) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PaymentViewModel::class.java)) {
            return PaymentViewModel(repository) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: PaymentViewModelFactory? = null
        fun getInstancePayment(context: Context): PaymentViewModelFactory {
            return instance ?: synchronized(this) {
                instance ?: PaymentViewModelFactory(Injection.provideRepositoryPayment(context))
            }.also { instance = it }
        }
    }
}