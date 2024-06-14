package com.example.tamanpempek.response

import android.os.Parcelable
import com.example.tamanpempek.model.UserModel
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class LoginResponse(
    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("msg")
    val msg: String,

    @field:SerializedName("token")
    val token: String,

    @field:SerializedName("data")
    val data: UserModel
) : Parcelable