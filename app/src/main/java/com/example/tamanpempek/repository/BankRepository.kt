package com.example.tamanpempek.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.tamanpempek.api.ApiService
import com.example.tamanpempek.helper.ResultCondition
import com.example.tamanpempek.request.BankCreateRequest
import com.example.tamanpempek.request.BankUpdateRequest
import com.example.tamanpempek.response.BankResponse
import com.example.tamanpempek.response.BanksResponse
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
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

    fun getBanksByUser(userId: Int): LiveData<ResultCondition<BanksResponse>> = liveData(
        Dispatchers.IO) {
        emit(ResultCondition.LoadingState)
        try {
            val response =
                apiService.getBanksByUser(userId).awaitResponse()

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

    fun getBankById(id: Int): LiveData<ResultCondition<BankResponse>> = liveData(
        Dispatchers.IO) {
        emit(ResultCondition.LoadingState)
        try {
            val response =
                apiService.getBankById(id).awaitResponse()

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

    fun createBank(userId: Int, name: String, type: String, number: String): LiveData<ResultCondition<BankResponse>> = liveData {
        emit(ResultCondition.LoadingState)
        try {
            val request = BankCreateRequest(userId, name, type, number)
            val json = Gson().toJson(request)
            val requestBody = json.toRequestBody("application/json".toMediaType())

            val response = apiService.createBank(requestBody)
            if (response.error) {
                emit(ResultCondition.ErrorState(response.msg))
            } else {
                emit(ResultCondition.SuccessState(response))
            }
        } catch (e: Exception) {
            emit(ResultCondition.ErrorState(e.message.toString()))
        }
    }

    fun updateBank(id: Int, name: String, type: String, number: String): LiveData<ResultCondition<BankResponse>> = liveData {
        emit(ResultCondition.LoadingState)
        try {
            val request = BankUpdateRequest(name, type, number)
            val json = Gson().toJson(request)
            val requestBody = json.toRequestBody("application/json".toMediaType())

            val response = apiService.updateBank(id, requestBody)
            if (response.error) {
                emit(ResultCondition.ErrorState(response.msg))
            } else {
                emit(ResultCondition.SuccessState(response))
            }
        } catch (e: Exception) {
            emit(ResultCondition.ErrorState(e.message.toString()))
        }
    }

    fun deleteBank(id: Int): LiveData<ResultCondition<BankResponse>> = liveData {
        emit(ResultCondition.LoadingState)
        try {
            val response = apiService.deleteBank(id)
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