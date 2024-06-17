package com.example.tamanpempek.viewmodel.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.tamanpempek.helper.Injection
import com.example.tamanpempek.repository.UserRepository
import com.example.tamanpempek.viewmodel.UserViewModel

class UserViewModelFactory private constructor(private val repository: UserRepository) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
            return UserViewModel(repository) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: UserViewModelFactory? = null
        fun getInstanceAuth(context: Context): UserViewModelFactory {
            return instance ?: synchronized(this) {
                instance ?: UserViewModelFactory(Injection.provideRepositoryAuth(context))
            }.also { instance = it }
        }
    }
}