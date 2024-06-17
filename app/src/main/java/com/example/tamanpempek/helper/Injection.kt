package com.example.tamanpempek.helper

import android.content.Context
import com.example.tamanpempek.api.ApiConfig
import com.example.tamanpempek.repository.BankRepository
import com.example.tamanpempek.repository.CartRepository
import com.example.tamanpempek.repository.ProductRepository
import com.example.tamanpempek.repository.UserRepository

object Injection {
    fun provideRepositoryAuth(context: Context): UserRepository {
        val api = ApiConfig.getApiService()
        return UserRepository.getInstance(api)
    }

    fun provideRepositoryProduct(context: Context): ProductRepository {
        val api = ApiConfig.getApiService()
        return ProductRepository.getInstance(api)
    }

    fun provideRepositoryBank(context: Context): BankRepository {
        val api = ApiConfig.getApiService()
        return BankRepository.getInstance(api)
    }

    fun provideRepositoryCart(context: Context): CartRepository {
        val api = ApiConfig.getApiService()
        return CartRepository.getInstance(api)
    }
}