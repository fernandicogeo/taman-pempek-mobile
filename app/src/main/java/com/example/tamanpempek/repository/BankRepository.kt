package com.example.tamanpempek.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.tamanpempek.api.ApiService
import com.example.tamanpempek.helper.ResultCondition
import com.example.tamanpempek.response.BankResponse
import com.example.tamanpempek.response.BanksResponse
import com.example.tamanpempek.response.ProductResponse
import kotlinx.coroutines.Dispatchers
import retrofit2.awaitResponse

class BankRepository(private val apiService: ApiService) {
    fun getBanks() : LiveData<ResultCondition<BanksResponse>> = liveData {
        emit(ResultCondition.LoadingState)
        try {
            val response = apiService.getBanks()
            if (response.error) {
                emit(ResultCondition.ErrorState(response.msg))
            } else {
                emit(ResultCondition.SuccessState(response))
            }
        } catch (e: Exception) {
            emit(ResultCondition.ErrorState(e.message.toString()))
        }
    }

    fun getBankById(userId: Int): LiveData<ResultCondition<BankResponse>> = liveData(
        Dispatchers.IO) {
        emit(ResultCondition.LoadingState)
        try {
            val response =
                apiService.getBankById(userId).awaitResponse()

            if (response.isSuccessful) {
                val bankResponse = response.body()
                if (bankResponse != null) {
                    emit(ResultCondition.SuccessState(bankResponse))
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
        private var instanceRepo: BankRepository? = null
        fun getInstance(api: ApiService): BankRepository =
            instanceRepo ?: synchronized(this) {
                instanceRepo ?: BankRepository(api)
            }.also {
                instanceRepo = it
            }
    }
}