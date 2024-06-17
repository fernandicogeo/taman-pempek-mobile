package com.example.tamanpempek.viewmodel.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.tamanpempek.helper.Injection
import com.example.tamanpempek.repository.CartRepository
import com.example.tamanpempek.viewmodel.CartViewModel

class CartViewModelFactory private constructor(private val repository: CartRepository) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CartViewModel::class.java)) {
            return CartViewModel(repository) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: CartViewModelFactory? = null
        fun getInstanceCart(context: Context): CartViewModelFactory {
            return instance ?: synchronized(this) {
                instance ?: CartViewModelFactory(Injection.provideRepositoryCart(context))
            }.also { instance = it }
        }
    }
}