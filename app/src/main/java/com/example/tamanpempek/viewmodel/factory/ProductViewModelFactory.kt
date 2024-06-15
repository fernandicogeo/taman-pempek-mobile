package com.example.tamanpempek.viewmodel.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.tamanpempek.helper.Injection
import com.example.tamanpempek.repository.ProductRepository
import com.example.tamanpempek.viewmodel.LoginViewModel
import com.example.tamanpempek.viewmodel.ProductViewModel
import com.example.tamanpempek.viewmodel.RegisterViewModel

class ProductViewModelFactory private constructor(private val repository: ProductRepository) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProductViewModel::class.java)) {
            return ProductViewModel(repository) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: ProductViewModelFactory? = null
        fun getInstanceProduct(context: Context): ProductViewModelFactory {
            return instance ?: synchronized(this) {
                instance ?: ProductViewModelFactory(Injection.provideRepositoryProduct(context))
            }.also { instance = it }
        }
    }
}