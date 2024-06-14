package com.example.tamanpempek.api

import com.example.tamanpempek.model.RegisterModel
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiService {
//    @POST("login")
//    @Headers("Content-Type: application/json")
//    suspend fun login(@Body requestBody: RequestBody) : LoginModel

    @POST("user/register")
    @Headers("Content-Type: application/json")
    suspend fun register(@Body requestBody: RequestBody): RegisterModel

}