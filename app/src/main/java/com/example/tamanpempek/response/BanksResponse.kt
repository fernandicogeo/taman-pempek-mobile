package com.example.tamanpempek.response

import android.os.Parcelable
import com.example.tamanpempek.model.BankModel
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class BanksResponse(
    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("msg")
    val msg: String,

    @field:SerializedName("data")
    val data: List<BankModel>
) : Parcelable