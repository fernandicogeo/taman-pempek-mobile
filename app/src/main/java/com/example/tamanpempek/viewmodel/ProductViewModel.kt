package com.example.tamanpempek.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.tamanpempek.repository.ProductRepository
import com.example.tamanpempek.request.ProductCreateRequest
import com.example.tamanpempek.request.ProductUpdateRequest
import com.example.tamanpempek.ui.seller.AddProductSellerActivity

class ProductViewModel(private val repository: ProductRepository) : ViewModel() {
    fun getProducts() = repository.getProducts()
    fun getUserProductsByCategory(userId: Int, categoryId: Int) = repository.getUserProductsByCategory(userId, categoryId)
    fun getProductById(userId: Int) = repository.getProductById(userId)
    fun createProduct(productCreateRequest: ProductCreateRequest, context: Context) = repository.createProduct(productCreateRequest, context)
    fun updateProduct(id: Int, productUpdateRequest: ProductUpdateRequest, context: Context) = repository.updateProduct(id, productUpdateRequest, context)
    fun deleteProduct(userId: Int) = repository.deleteProduct(userId)
}