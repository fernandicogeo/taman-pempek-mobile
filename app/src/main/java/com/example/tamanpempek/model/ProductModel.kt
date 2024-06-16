package com.example.tamanpempek.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProductModel(
    @SerializedName("id")
    val id: Int,

    @SerializedName("user_id")
    val user_id: Int,

    @SerializedName("category_id")
    val category_id: Int,

    @SerializedName("name")
    val name: String,

    @SerializedName("image")
    val image: String,

    @SerializedName("description")
    val description: String,

    @SerializedName("price")
    val price: Int,

    @SerializedName("stock")
    val stock: Int
) : Parcelable
