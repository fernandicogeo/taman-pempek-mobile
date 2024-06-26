package com.example.tamanpempek.viewmodel.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.tamanpempek.helper.Injection
import com.example.tamanpempek.repository.SettingRepository
import com.example.tamanpempek.viewmodel.SettingViewModel

class SettingViewModelFactory private constructor(private val repository: SettingRepository) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SettingViewModel::class.java)) {
            return SettingViewModel(repository) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: SettingViewModelFactory? = null
        fun getInstanceSetting(context: Context): SettingViewModelFactory {
            return instance ?: synchronized(this) {
                instance ?: SettingViewModelFactory(Injection.provideRepositorySetting(context))
            }.also { instance = it }
        }
    }
}