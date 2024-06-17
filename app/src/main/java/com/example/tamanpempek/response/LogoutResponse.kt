package com.example.tamanpempek.response

import android.os.Parcelable
import com.example.tamanpempek.model.UserModel
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class LogoutResponse(
    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("msg")
    val msg: String
) : Parcelable