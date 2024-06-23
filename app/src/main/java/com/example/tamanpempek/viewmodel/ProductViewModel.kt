package com.example.tamanpempek.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.tamanpempek.repository.ProductRepository
import com.example.tamanpempek.request.ProductCreateRequest
import com.example.tamanpempek.request.ProductUpdateRequest
import com.example.tamanpempek.request.ProductUpdateStockRequest

class ProductViewModel(private val repository: ProductRepository) : ViewModel() {
    fun getProducts() = repository.getProducts()
    fun getProductsByUser(userId: Int) = repository.getProductsByUser(userId)
    fun getProductsByCategory(categoryId: Int) = repository.getProductsByCategory(categoryId)
    fun getUserProductsByCategory(userId: Int, categoryId: Int) = repository.getUserProductsByCategory(userId, categoryId)
    fun getProductById(id: Int) = repository.getProductById(id)
    fun createProduct(productCreateRequest: ProductCreateRequest, context: Context) = repository.createProduct(productCreateRequest, context)
    fun updateProduct(id: Int, productUpdateRequest: ProductUpdateRequest, context: Context) = repository.updateProduct(id, productUpdateRequest, context)
    fun updateProductStock(id: Int, productUpdateStockRequest: ProductUpdateStockRequest) = repository.updateProductStock(id, productUpdateStockRequest)
    fun deleteProduct(userId: Int) = repository.deleteProduct(userId)
}