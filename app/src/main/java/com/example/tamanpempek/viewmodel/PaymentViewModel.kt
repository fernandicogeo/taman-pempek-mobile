package com.example.tamanpempek.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.tamanpempek.repository.PaymentRepository
import com.example.tamanpempek.request.PaymentCreateRequest

class PaymentViewModel(private val repository: PaymentRepository) : ViewModel() {
    fun createPayment(productCreateRequest: PaymentCreateRequest, context: Context) = repository.createPayment(productCreateRequest, context)
}