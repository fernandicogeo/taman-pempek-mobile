package com.example.tamanpempek.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.tamanpempek.api.ApiService
import com.example.tamanpempek.helper.ResultCondition
import com.example.tamanpempek.request.CartUpdateStatusRequest
import com.example.tamanpempek.request.CartCreateRequest
import com.example.tamanpempek.request.CartUpdatePaymentIdRequest
import com.example.tamanpempek.response.CartResponse
import com.example.tamanpempek.response.CartTotalPriceResponse
import com.example.tamanpempek.response.CartsResponse
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

    fun updateStatusCart(id: Int, isActived: String): LiveData<ResultCondition<CartResponse>> = liveData {
        emit(ResultCondition.LoadingState)
        try {
            val request = CartUpdateStatusRequest(isActived = isActived)
            val json = Gson().toJson(request)
            val requestBody = json.toRequestBody("application/json".toMediaType())

            val response = apiService.updateCart(id, requestBody)
            if (response.error) {
                emit(ResultCondition.ErrorState(response.msg))
            } else {
                emit(ResultCondition.SuccessState(response))
            }
        } catch (e: Exception) {
            emit(ResultCondition.ErrorState(e.message.toString()))
        }
    }

    fun updatePaymentIdCarts(id: Int, paymentId: Int): LiveData<ResultCondition<CartResponse>> = liveData {
        emit(ResultCondition.LoadingState)
        try {
            val request = CartUpdatePaymentIdRequest(payment_id = paymentId)
            val json = Gson().toJson(request)
            val requestBody = json.toRequestBody("application/json".toMediaType())

            val response = apiService.updateCart(id, requestBody)
            if (response.error) {
                emit(ResultCondition.ErrorState(response.msg))
            } else {
                emit(ResultCondition.SuccessState(response))
            }
        } catch (e: Exception) {
            emit(ResultCondition.ErrorState(e.message.toString()))
        }
    }

    fun getCartsByPaymentId(paymentId: Int): LiveData<ResultCondition<CartsResponse>> = liveData(
        Dispatchers.IO) {
        emit(ResultCondition.LoadingState)
        try {
            val response =
                apiService.getCartsByPaymentId(paymentId).awaitResponse()

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

    fun getCartsByProductId(productId: Int): LiveData<ResultCondition<CartsResponse>> = liveData(
        Dispatchers.IO) {
        emit(ResultCondition.LoadingState)
        try {
            val response =
                apiService.getCartsByProductId(productId).awaitResponse()

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

    fun getStatusCartByUser(isActived: String, userId: Int): LiveData<ResultCondition<CartsResponse>> = liveData(
        Dispatchers.IO) {
        emit(ResultCondition.LoadingState)
        try {
            val response =
                apiService.getStatusCartByUser(isActived, userId).awaitResponse()

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

    fun getCartsTotalPriceByUser(isActived: String, userId: Int): LiveData<ResultCondition<CartTotalPriceResponse>> = liveData(
        Dispatchers.IO) {
        emit(ResultCondition.LoadingState)
        try {
            val response =
                apiService.getCartsTotalPriceByUser(isActived, userId).awaitResponse()

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

    fun deleteCart(id: Int): LiveData<ResultCondition<CartResponse>> = liveData {
        emit(ResultCondition.LoadingState)
        try {
            val response = apiService.deleteCart(id)
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