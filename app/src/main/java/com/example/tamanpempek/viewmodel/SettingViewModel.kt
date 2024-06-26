package com.example.tamanpempek.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.tamanpempek.repository.SettingRepository
import com.example.tamanpempek.request.SettingRequest

class SettingViewModel(private val repository: SettingRepository) : ViewModel() {
    fun getSettingById(id: Int) = repository.getSettingById(id)
    fun updateSetting(id: Int, settingUpdateRequest: SettingRequest, context: Context) = repository.updateSetting(id, settingUpdateRequest, context)
}