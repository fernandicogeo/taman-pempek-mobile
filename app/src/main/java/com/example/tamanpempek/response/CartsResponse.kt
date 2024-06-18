package com.example.tamanpempek.response

import android.os.Parcelable
import com.example.tamanpempek.model.CartModel
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CartsResponse(
    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("msg")
    val msg: String,

    @field:SerializedName("data")
    val data: List<CartModel>
) : Parcelable