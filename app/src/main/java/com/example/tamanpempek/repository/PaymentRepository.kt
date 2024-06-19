package com.example.tamanpempek.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.tamanpempek.api.ApiService
import com.example.tamanpempek.helper.ResultCondition
import com.example.tamanpempek.request.CartCreateRequest
import com.example.tamanpempek.response.CartResponse
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody

class PaymentRepository(private val apiService: ApiService) {
    fun createCart(userId: Int, productId: Int, paymentId: Int?, quantity: Int, totalPrice: Int, isActived: String): LiveData<ResultCondition<CartResponse>> = liveData {
        emit(ResultCondition.LoadingState)
        try {
            val request = CartCreateRequest(userId, productId, paymentId, quantity, totalPrice, isActived)
            val json = Gson().toJson(request)
            val requestBody = json.toRequestBody("application/json".toMediaType())

            val response = apiService.createCart(requestBody)
            if (response.error) {
                emit(ResultCondition.ErrorState(response.msg))
            } else {
                emit(ResultCondition.SuccessState(response))
            }
        } catch (e: Exception) {
            emit(ResultCondition.ErrorState(e.message.toString()))
        }
    }

    companion object {
        @Volatile
        private var instanceRepo: CartRepository? = null
        fun getInstance(api: ApiService): CartRepository =
            instanceRepo ?: synchronized(this) {
                instanceRepo ?: CartRepository(api)
            }.also {
                instanceRepo = it
            }
    }
}