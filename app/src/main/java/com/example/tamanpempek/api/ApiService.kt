package com.example.tamanpempek.api

import com.example.tamanpempek.model.ProductModel
import com.example.tamanpempek.response.LoginResponse
import com.example.tamanpempek.response.ProductResponse
import com.example.tamanpempek.response.RegisterResponse
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @POST("login")
    @Headers("Content-Type: application/json")
    suspend fun login(@Body requestBody: RequestBody) : LoginResponse

    @POST("user/register")
    @Headers("Content-Type: application/json")
    suspend fun register(@Body requestBody: RequestBody): RegisterResponse

    @GET("products")
    suspend fun getProducts(): ProductResponse

    @GET("products/{userId}/{categoryId}")
    fun getUserProductsByCategory(
        @Path("userId") userId: Int,
        @Path("categoryId") categoryId: Int
    ): Call<ProductResponse>

}