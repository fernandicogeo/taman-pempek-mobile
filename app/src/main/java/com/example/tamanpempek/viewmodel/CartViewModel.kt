package com.example.tamanpempek.viewmodel

import androidx.lifecycle.ViewModel
import com.example.tamanpempek.repository.BankRepository
import com.example.tamanpempek.repository.CartRepository

class CartViewModel(private val repository: CartRepository) : ViewModel() {
    fun createCart(userId: Int, productId: Int, paymentId: Int?, quantity: Int, totalPrice: Int, isActived: String) = repository.createCart(userId, productId, paymentId, quantity, totalPrice, isActived)
    fun getActivedCartsByUser(userId: Int) = repository.getActivedCartsByUser(userId)
    fun getCartsTotalPriceByUser(userId: Int) = repository.getCartsTotalPriceByUser(userId)
    fun deleteCart(id: Int) = repository.deleteCart(id)
}