package com.example.tamanpempek.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class CartModel(
    @SerializedName("id")
    val id: Int,

    @SerializedName("user_id")
    val user_id: Int,

    @SerializedName("product_id")
    val product_id: Int,

    @SerializedName("payment_id")
    val payment_id: Int,

    @SerializedName("quantity")
    val quantity: Int,

    @SerializedName("total_price")
    val total_price: Int,

    @SerializedName("isActived")
    val isActived: String  // actived, checkouted, canceled, paid
) : Parcelable