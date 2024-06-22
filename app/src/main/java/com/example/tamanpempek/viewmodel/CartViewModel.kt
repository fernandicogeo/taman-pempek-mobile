package com.example.tamanpempek.viewmodel

import androidx.lifecycle.ViewModel
import com.example.tamanpempek.repository.BankRepository
import com.example.tamanpempek.repository.CartRepository

class CartViewModel(private val repository: CartRepository) : ViewModel() {
    fun createCart(userId: Int, productId: Int, paymentId: Int?, quantity: Int, totalPrice: Int, isActived: String) = repository.createCart(userId, productId, paymentId, quantity, totalPrice, isActived)
    fun updateStatusCart(id: Int, isActived: String) = repository.updateStatusCart(id, isActived)
    fun updatePaymentIdCarts(id: Int, paymentId: Int) = repository.updatePaymentIdCarts(id, paymentId)
    fun getCartsByPaymentId(paymentId: Int) = repository.getCartsByPaymentId(paymentId)
    fun getCartsByProductId(productId: Int) = repository.getCartsByProductId(productId)
    fun getCartById(id: Int) = repository.getCartById(id)
    fun getStatusCartByUser(isActived: String, userId: Int) = repository.getStatusCartByUser(isActived, userId)
    fun getCartsTotalPriceByUser(isActived: String, userId: Int) = repository.getCartsTotalPriceByUser(isActived, userId)
    fun deleteCart(id: Int) = repository.deleteCart(id)
}