package com.example.tamanpempek.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.tamanpempek.api.ApiService
import com.example.tamanpempek.helper.ResultCondition
import com.example.tamanpempek.request.LoginRequest
import com.example.tamanpempek.response.RegisterResponse
import com.example.tamanpempek.request.RegisterRequest
import com.example.tamanpempek.response.LoginResponse
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody

class UserRepository(private val apiService: ApiService) {
    fun login(userEmail: String, userPassword: String) : LiveData<ResultCondition<LoginResponse>> = liveData {
        emit(ResultCondition.LoadingState)
        try {
            val request = LoginRequest(userEmail, userPassword)
            val json = Gson().toJson(request)
            val requestBody = json.toRequestBody("application/json".toMediaType())

            val response = apiService.login(requestBody)
            if (response.error) {
                emit(ResultCondition.ErrorState(response.msg))
            } else {
                emit(ResultCondition.SuccessState(response))
            }
        } catch (e: Exception) {
            emit(ResultCondition.ErrorState(e.message.toString()))
        }
    }

    fun register(userName: String, userEmail: String, userPassword: String, userWhatsapp: String, userGender: String, userRole: String): LiveData<ResultCondition<RegisterResponse>> = liveData {
        emit(ResultCondition.LoadingState)
        try {
            val request = RegisterRequest(userName, userEmail, userPassword, userWhatsapp, userGender, userRole)
            val json = Gson().toJson(request)
            val requestBody = json.toRequestBody("application/json".toMediaType())

            val response = apiService.register(requestBody)
            if (response.error) {
                emit(ResultCondition.ErrorState(response.msg))
            } else {
                emit(ResultCondition.SuccessState(response))
            }
        } catch (e: Exception) {
            emit(ResultCondition.ErrorState(e.message.toString()))
        }
    }

//    fun edit(userName: String, userEmail: String, userAddress: String?, userNohp: String?, userPassword: String?): LiveData<ResultCondition<EditModel>> = liveData {
//        emit(ResultCondition.LoadingState)
//        try {
//            val request = EditRequest(userName, userEmail, userAddress, userNohp, userPassword)
//            val json = Gson().toJson(request)
//            val requestBody = json.toRequestBody("application/json".toMediaType())
//
//            val response = apiService.edit(requestBody)
//            if (response.error.toBoolean()) {
//                emit(ResultCondition.ErrorState(response.msg))
//            } else {
//                emit(ResultCondition.SuccessState(response))
//            }
//        } catch (e: Exception) {
//            emit(ResultCondition.ErrorState(e.message.toString()))
//        }
//    }

    companion object {
        @Volatile
        private var instanceRepo: UserRepository? = null
        fun getInstance(api: ApiService): UserRepository =
            instanceRepo ?: synchronized(this) {
                instanceRepo ?: UserRepository(api)
            }.also {
                instanceRepo = it
            }
    }
}