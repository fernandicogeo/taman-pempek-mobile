package com.example.tamanpempek.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class BankModel(
    @SerializedName("id")
    val id: Int,

    @SerializedName("user_id")
    val user_id: Int,

    @SerializedName("name")
    val name: String,

    @SerializedName("type")
    val type: String,

    @SerializedName("number")
    val number: String,
) : Parcelable