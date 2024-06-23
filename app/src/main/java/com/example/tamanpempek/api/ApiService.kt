package com.example.tamanpempek.api

import com.example.tamanpempek.response.BankResponse
import com.example.tamanpempek.response.BanksResponse
import com.example.tamanpempek.response.CartResponse
import com.example.tamanpempek.response.CartTotalPriceResponse
import com.example.tamanpempek.response.CartsResponse
import com.example.tamanpempek.response.LoginResponse
import com.example.tamanpempek.response.LogoutResponse
import com.example.tamanpempek.response.PaymentResponse
import com.example.tamanpempek.response.PaymentsResponse
import com.example.tamanpempek.response.ProductResponse
import com.example.tamanpempek.response.ProductsResponse
import com.example.tamanpempek.response.UserResponse
import com.example.tamanpempek.response.UsersResponse
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
    suspend fun register(@Body requestBody: RequestBody): UserResponse

    @POST("logout")
    @Headers("Content-Type: application/json")
    suspend fun logout() : LogoutResponse

    @GET("user/{userId}")
    fun getUserById(
        @Path("userId") userId: Int,
    ): Call<UserResponse>

    @GET("users/role/{role}")
    fun getUsersByRole(
        @Path("role") role: String,
    ): Call<UsersResponse>

    @PUT("user/update/{id}")
    @Headers("Content-Type: application/json")
    suspend fun updateUser(
        @Path("id") id: Int,
        @Body requestBody: RequestBody): UserResponse

    @DELETE("user/delete/{userId}")
    suspend fun deleteUser(
        @Path("userId") userId: Int
    ): UserResponse

    @GET("products")
    suspend fun getProducts(): ProductsResponse

    @GET("products/{userId}")
    fun getProductsByUser(
        @Path("userId") userId: Int,
    ): Call<ProductsResponse>

    @GET("products/category/{categoryId}")
    fun getProductsByCategory(
        @Path("categoryId") categoryId: Int,
    ): Call<ProductsResponse>

    @GET("products/{userId}/{categoryId}")
    fun getUserProductsByCategory(
        @Path("userId") userId: Int,
        @Path("categoryId") categoryId: Int
    ): Call<ProductsResponse>

    @GET("product/{id}")
    fun getProductById(
        @Path("id") id: Int,
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

    @GET("banks")
    suspend fun getBanks(): BanksResponse

    @GET("banks/{userId}")
    fun getBanksByUser(
        @Path("userId") userId: Int,
    ): Call<BanksResponse>

    @GET("banks/admin")
    fun getAdminBanks(): Call<BanksResponse>

    @GET("bank/{id}")
    fun getBankById(
        @Path("id") id: Int,
    ): Call<BankResponse>

    @POST("bank/create")
    @Headers("Content-Type: application/json")
    suspend fun createBank(@Body requestBody: RequestBody): BankResponse

    @PUT("bank/update/{id}")
    @Headers("Content-Type: application/json")
    suspend fun updateBank(
        @Path("id") id: Int,
        @Body requestBody: RequestBody): BankResponse

    @DELETE("bank/delete/{id}")
    suspend fun deleteBank(
        @Path("id") id: Int
    ): BankResponse

    @GET("carts/{isActived}/{userId}")
    fun getStatusCartByUser(
        @Path("isActived") isActived: String,
        @Path("userId") userId: Int,
    ): Call<CartsResponse>

    @GET("carts/payment/{paymentId}")
    fun getCartsByPaymentId(
        @Path("paymentId") paymentId: Int,
    ): Call<CartsResponse>

    @GET("carts/product/{productId}")
    fun getCartsByProductId(
        @Path("productId") productId: Int,
    ): Call<CartsResponse>

    @GET("cart/{id}")
    fun getCartById(
        @Path("id") id: Int,
    ): Call<CartResponse>

    @GET("carts/total/{isActived}/{userId}")
    fun getCartsTotalPriceByUser(
        @Path("isActived") isActived: String,
        @Path("userId") userId: Int,
    ): Call<CartTotalPriceResponse>

    @POST("cart/create")
    @Headers("Content-Type: application/json")
    suspend fun createCart(@Body requestBody: RequestBody): CartResponse

    @PUT("cart/update/{id}")
    @Headers("Content-Type: application/json")
    suspend fun updateCart(
        @Path("id") id: Int,
        @Body requestBody: RequestBody): CartResponse

    @DELETE("cart/delete/{id}")
    suspend fun deleteCart(
        @Path("id") id: Int
    ): CartResponse

    @Multipart
    @POST("payment/create")
    suspend fun createPayment(
        @Part("user_id") userId: RequestBody,
        @Part("delivery_id") deliveryId: RequestBody,
        @Part("total_price") totalPrice: RequestBody,
        @Part image: MultipartBody.Part,
        @Part("address") address: RequestBody,
        @Part("whatsapp") whatsapp: RequestBody,
        @Part("payment_status") paymentStatus: RequestBody,
        @Part("delivery_name") deliveryName: RequestBody,
    ): PaymentResponse

    @GET("payments/{userId}/{paymentStatus}")
    fun getPaymentsByUserAndPaymentStatus(
        @Path("userId") userId: Int,
        @Path("paymentStatus") paymentStatus: String,
    ): Call<PaymentsResponse>

    @GET("payment/{id}")
    fun getPaymentById(
        @Path("id") id: Int,
    ): Call<PaymentResponse>

    @Multipart
    @PUT("payment/update/{id}")
    suspend fun updatePaymentStatus(
        @Path("id") id: Int,
        @Part("payment_status") paymentStatus: RequestBody,
    ): PaymentResponse

    @Multipart
    @PUT("payment/update/{id}")
    suspend fun updatePaymentStatusAndDelivery(
        @Path("id") id: Int,
        @Part("payment_status") paymentStatus: RequestBody,
        @Part("delivery_name") deliveryName: RequestBody,
    ): PaymentResponse
}