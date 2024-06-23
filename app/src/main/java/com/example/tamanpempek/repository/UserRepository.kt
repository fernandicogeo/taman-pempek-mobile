package com.example.tamanpempek.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.tamanpempek.api.ApiService
import com.example.tamanpempek.helper.ResultCondition
import com.example.tamanpempek.request.LoginRequest
import com.example.tamanpempek.response.UserResponse
import com.example.tamanpempek.request.RegisterRequest
import com.example.tamanpempek.request.UserUpdateRequest
import com.example.tamanpempek.response.LoginResponse
import com.example.tamanpempek.response.LogoutResponse
import com.example.tamanpempek.response.UsersResponse
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.awaitResponse

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

    fun register(userName: String, userEmail: String, userPassword: String, userWhatsapp: String, userGender: String, userRole: String): LiveData<ResultCondition<UserResponse>> = liveData {
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

    fun logout() : LiveData<ResultCondition<LogoutResponse>> = liveData {
        emit(ResultCondition.LoadingState)
        try {
            val response = apiService.logout()
            if (response.error) {
                emit(ResultCondition.ErrorState(response.msg))
            } else {
                emit(ResultCondition.SuccessState(response))
            }
        } catch (e: Exception) {
            emit(ResultCondition.ErrorState(e.message.toString()))
        }
    }

    fun getUserById(userId: Int): LiveData<ResultCondition<UserResponse>> = liveData(
        Dispatchers.IO) {
        emit(ResultCondition.LoadingState)
        try {
            val response =
                apiService.getUserById(userId).awaitResponse()

            if (response.isSuccessful) {
                val userResponse = response.body()
                if (userResponse != null) {
                    emit(ResultCondition.SuccessState(userResponse))
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

    fun getUsersByRole(role: String): LiveData<ResultCondition<UsersResponse>> = liveData(
        Dispatchers.IO) {
        emit(ResultCondition.LoadingState)
        try {
            val response =
                apiService.getUsersByRole(role).awaitResponse()

            if (response.isSuccessful) {
                val userResponse = response.body()
                if (userResponse != null) {
                    emit(ResultCondition.SuccessState(userResponse))
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

    fun updateUser(id: Int, userName: String, userEmail: String, userPassword: String, userWhatsapp: String, userGender: String): LiveData<ResultCondition<UserResponse>> = liveData {
        emit(ResultCondition.LoadingState)
        try {
            val request = UserUpdateRequest(userName, userEmail, userPassword, userWhatsapp, userGender)
            val json = Gson().toJson(request)
            val requestBody = json.toRequestBody("application/json".toMediaType())

            val response = apiService.updateUser(id, requestBody)
            if (response.error) {
                emit(ResultCondition.ErrorState(response.msg))
            } else {
                emit(ResultCondition.SuccessState(response))
            }
        } catch (e: Exception) {
            emit(ResultCondition.ErrorState(e.message.toString()))
        }
    }

    fun deleteUser(userId: Int): LiveData<ResultCondition<UserResponse>> = liveData {
        emit(ResultCondition.LoadingState)
        try {
            val response = apiService.deleteUser(userId)
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
        private var instanceRepo: UserRepository? = null
        fun getInstance(api: ApiService): UserRepository =
            instanceRepo ?: synchronized(this) {
                instanceRepo ?: UserRepository(api)
            }.also {
                instanceRepo = it
            }
    }
}