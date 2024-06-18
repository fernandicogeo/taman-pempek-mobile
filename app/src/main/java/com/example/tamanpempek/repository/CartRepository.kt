package com.example.tamanpempek.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.tamanpempek.api.ApiService
import com.example.tamanpempek.helper.ResultCondition
import com.example.tamanpempek.request.CartCreateRequest
import com.example.tamanpempek.response.CartsResponse
import com.example.tamanpempek.response.CartResponse
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.awaitResponse

class CartRepository(private val apiService: ApiService) {
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

    fun getActivedCartsByUser(userId: Int): LiveData<ResultCondition<CartsResponse>> = liveData(
        Dispatchers.IO) {
        emit(ResultCondition.LoadingState)
        try {
            val response =
                apiService.getActivedCartsByUser(userId).awaitResponse()

            if (response.isSuccessful) {
                val cartResponse = response.body()
                if (cartResponse != null) {
                    emit(ResultCondition.SuccessState(cartResponse))
                } else {
                    emit(ResultCondition.ErrorState("Empty response body"))
                }
            } else {
                val errorBody = response.errorBody()?.string()
                val errorMsg = errorBody ?: "Unknown error occurred"
                emit(ResultCondition.ErrorState(errorMsg))
            }
        } catch (e: Exception) {
            emit(ResultCondition.ErrorState(e.message ?: "Error occurred"))
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