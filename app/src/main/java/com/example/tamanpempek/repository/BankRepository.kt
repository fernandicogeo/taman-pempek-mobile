package com.example.tamanpempek.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.tamanpempek.api.ApiService
import com.example.tamanpempek.helper.ResultCondition
import com.example.tamanpempek.response.BanksResponse

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