package com.example.tamanpempek.api

import com.example.tamanpempek.response.LoginResponse
import com.example.tamanpempek.response.ProductResponse
import com.example.tamanpempek.response.ProductsResponse
import com.example.tamanpempek.response.RegisterResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

interface ApiService {
    @POST("login")
    @Headers("Content-Type: application/json")
    suspend fun login(@Body requestBody: RequestBody) : LoginResponse

    @POST("user/register")
    @Headers("Content-Type: application/json")
    suspend fun register(@Body requestBody: RequestBody): RegisterResponse

    @GET("products")
    suspend fun getProducts(): ProductsResponse

    @GET("products/{userId}/{categoryId}")
    fun getUserProductsByCategory(
        @Path("userId") userId: Int,
        @Path("categoryId") categoryId: Int
    ): Call<ProductsResponse>

    @GET("product/{userId}")
    fun getProductById(
        @Path("userId") userId: Int,
    ): Call<ProductResponse>

    @Multipart
    @POST("product/create")
    suspend fun createProduct(
        @Part("user_id") userId: RequestBody,
        @Part("category_id") categoryId: RequestBody,
        @Part("name") name: RequestBody,
        @Part image: MultipartBody.Part,
        @Part("description") description: RequestBody,
        @Part("price") price: RequestBody,
        @Part("stock") stock: RequestBody
    ): ProductResponse

    @Multipart
    @PUT("product/update/{id}")
    suspend fun updateProduct(
        @Path("id") id: Int,
        @Part("user_id") userId: RequestBody,
        @Part("category_id") categoryId: RequestBody,
        @Part("name") name: RequestBody,
        @Part image: MultipartBody.Part?,
        @Part("description") description: RequestBody,
        @Part("price") price: RequestBody,
        @Part("stock") stock: RequestBody
    ): ProductResponse

    @DELETE("product/delete/{userId}")
    suspend fun deleteProduct(
        @Path("userId") userId: Int
    ): ProductResponse
}