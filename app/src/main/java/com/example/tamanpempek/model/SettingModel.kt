package com.example.tamanpempek.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class SettingModel(
    @SerializedName("image")
    val image: String,

    @SerializedName("description")
    val description: String,

    @SerializedName("contact")
    val contact: String

) : Parcelable
