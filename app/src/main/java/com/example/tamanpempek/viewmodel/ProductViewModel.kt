package com.example.tamanpempek.viewmodel

import androidx.lifecycle.ViewModel
import com.example.tamanpempek.repository.ProductRepository

class ProductViewModel(private val repository: ProductRepository) : ViewModel() {
    fun getProducts() = repository.getProducts()

    fun getUserProductsByCategory(userId: Int, categoryId: Int) = repository.getUserProductsByCategory(userId, categoryId)
}