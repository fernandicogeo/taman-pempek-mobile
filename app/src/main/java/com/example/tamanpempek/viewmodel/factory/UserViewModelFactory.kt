package com.example.tamanpempek.viewmodel.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.tamanpempek.helper.Injection
import com.example.tamanpempek.repository.UserRepository
import com.example.tamanpempek.viewmodel.LoginViewModel
import com.example.tamanpempek.viewmodel.RegisterViewModel

class UserViewModelFactory private constructor(private val repository: UserRepository) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
            return RegisterViewModel(repository) as T
        } else if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(repository) as T
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