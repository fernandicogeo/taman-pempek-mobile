package com.example.tamanpempek.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.tamanpempek.api.ApiService
import com.example.tamanpempek.helper.ResultCondition
import com.example.tamanpempek.response.ProductResponse
import kotlinx.coroutines.Dispatchers
import retrofit2.awaitResponse

class ProductRepository(private val apiService: ApiService) {
    fun getProducts() : LiveData<ResultCondition<ProductResponse>> = liveData {
        emit(ResultCondition.LoadingState)
        try {
            val response = apiService.getProducts()
            if (response.error) {
                emit(ResultCondition.ErrorState(response.msg))
            } else {
                emit(ResultCondition.SuccessState(response))
            }
        } catch (e: Exception) {
            emit(ResultCondition.ErrorState(e.message.toString()))
        }
    }

    fun getUserProductsByCategory(userId: Int, categoryId: Int): LiveData<ResultCondition<ProductResponse>> = liveData(
        Dispatchers.IO) {
        emit(ResultCondition.LoadingState)
        try {
            val response =
                apiService.getUserProductsByCategory(userId, categoryId).awaitResponse()

            if (response.isSuccessful) {
                val productResponse = response.body()
                if (productResponse != null) {
                    emit(ResultCondition.SuccessState(productResponse))
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
        private var instanceRepo: ProductRepository? = null
        fun getInstance(api: ApiService): ProductRepository =
            instanceRepo ?: synchronized(this) {
                instanceRepo ?: ProductRepository(api)
            }.also {
                instanceRepo = it
            }
    }
}