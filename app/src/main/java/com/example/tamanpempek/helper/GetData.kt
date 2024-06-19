package com.example.tamanpempek.helper

import androidx.lifecycle.LifecycleOwner
import com.example.tamanpempek.viewmodel.UserViewModel

object GetData {
    fun getSellerName(
        userViewModel: UserViewModel,
        lifecycleOwner: LifecycleOwner,
        userId: Int,
        callback: (String) -> Unit) {
        userViewModel.getUserById(userId).observe(lifecycleOwner) { result ->
            when (result) {
                is ResultCondition.LoadingState -> {
                }
                is ResultCondition.SuccessState -> {
                    val sellerName = result.data.data.name ?: ""
                    callback(sellerName)
                }
                is ResultCondition.ErrorState -> {
                    callback("")
                }
            }
        }
    }
}