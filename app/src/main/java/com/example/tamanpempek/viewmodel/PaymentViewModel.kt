package com.example.tamanpempek.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.tamanpempek.repository.PaymentRepository
import com.example.tamanpempek.request.PaymentCreateRequest
import com.example.tamanpempek.request.PaymentUpdateStatusRequest

class PaymentViewModel(private val repository: PaymentRepository) : ViewModel() {
    fun getPaymentById(id: Int) = repository.getPaymentById(id)
    fun getPaymentsByUserAndPaymentStatus(userId: Int, paymentStatus: String) = repository.getPaymentsByUserAndPaymentStatus(userId, paymentStatus)
    fun createPayment(paymentCreateRequest: PaymentCreateRequest, context: Context) = repository.createPayment(paymentCreateRequest, context)
    fun updatePaymentStatus(id: Int, paymentUpdateStatusRequest: PaymentUpdateStatusRequest) = repository.updatePaymentStatus(id, paymentUpdateStatusRequest,)
}