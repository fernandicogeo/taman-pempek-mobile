package com.example.tamanpempek.helper

import android.content.Context
import com.example.tamanpempek.api.ApiConfig
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
}